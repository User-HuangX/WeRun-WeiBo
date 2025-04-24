package org.example.post.config;

import jakarta.servlet.DispatcherType;
import org.example.post.Filter.TokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Tokenconfig {
    @Bean
    public FilterRegistrationBean<TokenFilter> tokenFilter() {
        FilterRegistrationBean<TokenFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TokenFilter());
        registration.addUrlPatterns("/post/editPost/*");
        registration.addUrlPatterns("/comment/toPost");
        registration.addUrlPatterns("/comment/toComment");
        registration.addUrlPatterns("/comment/reply");
        registration.addUrlPatterns("/comment/like");
        registration.addUrlPatterns("/comment/cancellike");
        registration.addUrlPatterns("/comment/deleteMyCommentsCTC");
        registration.addUrlPatterns("/comment/deleteMyCommentsCTP");
        registration.addUrlPatterns("/comment/deleteUnderPost");
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }
}
