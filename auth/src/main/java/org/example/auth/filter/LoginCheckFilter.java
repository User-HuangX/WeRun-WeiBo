//package org.example.auth.Filter;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.*;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.auth.Utils.JwtUtils;
//import org.example.common.model.global.AppException;
//import org.example.common.model.global.HttpStatus;
//
//import org.springframework.data.redis.core.StringRedisTemplate;
//
//import java.io.IOException;
//
///**
// * 登录检查过滤器
// * 用于全局过滤，检查用户是否登录
// */
//@Slf4j
//@RequiredArgsConstructor
//public class LoginCheckFilter implements Filter {
//    private final StringRedisTemplate stringRedisTemplate;
//
//    /**
//     * 执行过滤操作
//     * 检查用户是否登录，以及是否有权限访问请求的资源
//     *
//     * @param servletRequest  Servlet请求对象
//     * @param filterChain     过滤链，用于放行请求
//     */
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        // 将请求转换为HTTP类型
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        // 获取请求的URL和远程地址
//        String url = req.getRequestURI();
//        String remoteAddr = req.getRemoteAddr();
//
//        // 记录请求的URL和来源IP
//        log.info("请求的URL: {}, 来自 IP: {}", url, remoteAddr);
//
//        // 对于认证请求，直接放行
//        if (url.contains("/auth")){
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//
//        // 从请求头中获取JWT令牌
//        String token = req.getHeader("Authorization");
//        if (token == null || !token.startsWith("Bearer ")) {
//            // 返回401未授权
//            throw new AppException(HttpStatus.UNAUTHORIZED,"未授权",null);
//        }
//        String jwt = token.substring(7); // 去除"Bearer "前缀
//        String userId;
//
//            // 解析JWT令牌
//            Claims claims = JwtUtils.parseJWT(jwt);
//            log.info("JWT解析为：{}",claims.toString());
//            // 获取用户角色和ID
//            String role = claims.get("roles").toString();
//            userId = claims.get("userId").toString();
//            req.setAttribute("userId", userId);
//            String redisKey = "jwt:" + userId;
//            String redisValue = stringRedisTemplate.opsForValue().get(redisKey);
//        // 如果请求头中的token为空，返回未登录信息
//            if(redisValue==null || redisValue.isEmpty()){
//                throw new AppException(HttpStatus.UNAUTHORIZED,"未授权",null);
//            }
//            if (!stringRedisTemplate.hasKey(redisKey) || !redisValue.trim().equals(jwt)) {
//                throw new AppException(HttpStatus.UNAUTHORIZED,"未授权",null);
//            }
//
//        // 令牌合法，放行
//        log.info("令牌合法, 放行, 用户ID: {}, 来自 IP: {}", userId, remoteAddr);
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//
//}
