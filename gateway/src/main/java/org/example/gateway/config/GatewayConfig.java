package org.example.gateway.config;

import io.jsonwebtoken.Claims;

import jakarta.servlet.http.HttpServletRequest;
import org.example.gateway.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Configuration
public class GatewayConfig implements GlobalFilter {
    private static final Set<String> WHITELIST_URLS = new HashSet<>(Arrays.asList(
            "/post/push/pushPosts",
            "/auth/*",
            "/post/push/*",
            "/post/search/*",
            "/post/usrpost/*",
            "/comment/getCommentsByTime",
            "/comment/getCommentsByLikes"
            // 可以添加更多需要白名单的URL
    ));
    private final StringRedisTemplate stringRedisTemplate;

    public GatewayConfig(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    Logger logger = LoggerFactory.getLogger(GatewayConfig.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();

        // 检查是否在白名单中
        if (WHITELIST_URLS.stream().anyMatch(path::startsWith)) {
            logger.info("Path [{}] is in whitelist, bypassing authentication.", path);
            return chain.filter(exchange);
        }
        logger.debug("现在在网关过滤器");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        logger.info("获取到请求头为:{}", token);
        if (token == null || !token.startsWith("Bearer ")) {
            logger.error("请求头为空或者请求头的开头没有加上Bearer前缀");
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        }
        logger.debug("完成请求头规范校验");
        String jwt = token.substring(7);
        logger.info("成功获取到JWT为:{}", jwt);
        if (Objects.equals(stringRedisTemplate.opsForValue().get("blacklist:jwt:" + jwt), "BLACK")) {
            logger.error("请求头被列入了黑名单");
            throw new RuntimeException("Token has been blacklisted");
        }
        logger.debug("黑名单检测完成");

        return chain.filter(exchange);

    }

}

