package org.example.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PostMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostMainApplication.class, args);
    }
}
