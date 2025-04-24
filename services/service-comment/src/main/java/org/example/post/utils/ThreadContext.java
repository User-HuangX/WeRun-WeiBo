package org.example.post.utils;

public class ThreadContext {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setThreadLocal(String token) {
        contextHolder.set(token);
    }

    public static String getThreadLocal() {
        return contextHolder.get();
    }

    public static void removeThreadLocal() {
        contextHolder.remove();
    }
}
