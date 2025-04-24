package org.example.post.Filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.example.post.utils.JwtUtils;
import org.example.post.utils.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;


public class TokenFilter implements Filter {
    //初始化Logger
    public static final Logger Logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        Logger.debug("初始化过滤器");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 类型转换并验证
        if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
            throw new IllegalArgumentException("Invalid request or response type");
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取并验证 token
        String token = request.getHeader("Authorization");
        if (isInvalidToken(token)) {
            sendUnauthorizedResponse(response, "请求头为空或者请求头开头没有加上Bearer前缀");
            return;
        }
        while (true) {

            try {
                Claims claims = JwtUtils.parseJWT(extractToken(token));
                Logger.debug("成功解析JWT令牌");
                System.out.println(token);
                ThreadContext.setThreadLocal(token);
                break;


            } catch (Exception e) {
                try {
                    String refreshToken = request.getHeader("Authorization0");
                    Logger.debug("获取刷新令牌");
                    System.out.println(refreshToken);
                    ThreadContext.setThreadLocal(refreshToken);
                    Logger.debug("刷新token");
                    Claims claims1;
                    Logger.debug("开始刷新token");
                    claims1 = JwtUtils.parseJWT(extractToken(refreshToken));
                    Logger.debug("成功解析refreshToken");
                    token = refreshToken;
                    Logger.debug("刷新token成功");
                    ThreadContext.setThreadLocal(token);
                    refreshToken = JwtUtils.generateJwt(claims1);
                    Logger.debug("刷新token成功");
                    request = new CustomHttpServletRequestWrapper(request, token);
                } catch (Exception ex) {
                    sendUnauthorizedResponse(response, "刷新令牌无效");
                    return;
                }


            }
        }


//
        //放行请求
        filterChain.doFilter(request, response);
        Logger.debug("放行请求");


    }

    private static class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final String updatedToken;
        private boolean isRefreshed = false;

        public CustomHttpServletRequestWrapper(HttpServletRequest request, String updatedToken) {
            super(request);
            this.updatedToken = updatedToken;
        }

        @Override
        public String getHeader(String name) {
            if (!isRefreshed && "Authorization".equalsIgnoreCase(name)) {
                isRefreshed = true;
                return "Bearer " + updatedToken;
            }
            return super.getHeader(name);
        }
    }



    // 辅助方法：验证 token 是否有效
    private boolean isInvalidToken(String token) {
        return token == null || token.isEmpty() || !token.startsWith("Bearer ") || extractToken(token).isEmpty();
    }

    // 辅助方法：提取实际的令牌内容
    private String extractToken(String token) {
        return token.substring("Bearer ".length()).trim();
    }

    // 辅助方法：发送 401 响应
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(401);
        PrintWriter writer = response.getWriter();
        writer.write(message);
        writer.flush();
        writer.close();
        Logger.debug(message);
    }




    @Override
    public void destroy() {
        Logger.debug("销毁过滤器");
    }
}
