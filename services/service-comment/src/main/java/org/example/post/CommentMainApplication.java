package org.example.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 这是授权服务器，所有请求会拦截到这里进行重定向到登录接口
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableAsync
public class CommentMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommentMainApplication.class, args);
    }
}
