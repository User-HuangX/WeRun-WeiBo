package org.example.post.utils;

public class ThreadContext {

    static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static String getThreadLocal() {
        return threadLocal.get();
    }

    public static void setThreadLocal(String threadLocal) {
        ThreadContext.threadLocal.set(threadLocal);
    }

    public void clear() {
        threadLocal.remove();
    }
}
