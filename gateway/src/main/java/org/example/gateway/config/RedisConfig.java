package org.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类，用于定义与Redis相关的Bean
 */
@Configuration
public class RedisConfig {
    /**
     * 创建自定义的RedisTemplate实例
     * @param factory Redis连接工厂，用于创建和管理Redis连接
     * @return RedisTemplate实例，用于执行与Redis相关的操作
     */
    @Bean
    public RedisTemplate<String, Object> customRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 设置键的序列化器
        template.setKeySerializer(new StringRedisSerializer());

        // 设置值的序列化器
        template.setValueSerializer(new StringRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }
}
