package org.example.post.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 默认使用SimpleAsyncTaskExecutor（无复用线程），需自定义高性能线程池：
 * 根据任务类型（CPU/I/O密集型）调整线程池参数。
 * 避免队列容量过大导致内存溢出，推荐使用CallerRunsPolicy拒绝策略
 */

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);    // 核心线程数
        executor.setMaxPoolSize(50);     // 最大线程数
        executor.setQueueCapacity(100);  // 队列容量
        executor.setThreadNamePrefix("Async-"); // 线程名前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            // 全局异常处理
            System.err.println("异步异常：" + ex.getMessage());
        };
    }
}