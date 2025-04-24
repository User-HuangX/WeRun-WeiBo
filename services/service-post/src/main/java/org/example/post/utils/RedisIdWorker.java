package org.example.post.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式ID生成器
 * 用于生成全局唯一的ID，通过Redis来保证同一时间同一前缀的ID不重复
 */
@Component
public class RedisIdWorker {
    // 开始时间戳，用于计算偏移量
    private static final long BEGIN_TIMESTAMP = 1739933334L;
    // Redis模板，用于操作Redis
    private final StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 生成下一个ID
     * 根据给定的键前缀和当前日期生成一个全局唯一的ID
     *
     * @param keyPrefix 键前缀，用于区分不同业务的ID
     * @return 生成的ID
     */
    public long nextId(String keyPrefix) {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 将当前时间转换为秒
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        // 计算时间戳偏移量
        long timeStamp = nowSecond - BEGIN_TIMESTAMP;

        // 格式化当前日期为yyyyMMdd格式
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 构造Redis键
        String key = "icr:" + keyPrefix + ":" + date;

        // 如果Redis中不存在该键，则初始化为0
        if (!stringRedisTemplate.hasKey(key)) {
            stringRedisTemplate.opsForValue().set(key, "0",3600, TimeUnit.SECONDS);
        }

        // 对键进行自增操作,避免时间戳之差重复
        Long count = stringRedisTemplate.opsForValue().increment(key);
        // 如果自增失败，则抛出异常
        if (count == null) {
            throw new RuntimeException("获取全局唯一ID失败: " + key);
        }
        // 将时间戳偏移量和自增计数组合成一个64位的ID
        return (timeStamp << 32) | count;
    }
}
