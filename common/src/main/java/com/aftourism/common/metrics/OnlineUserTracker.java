package com.aftourism.common.metrics;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

/**
 * Keeps track of currently active sessions for simple online user counting.
 */
@Component
public class OnlineUserTracker {

    private final AtomicInteger onlineUsers = new AtomicInteger();

    public void increment() {
        onlineUsers.incrementAndGet();
    }

    public void decrement() {
        onlineUsers.updateAndGet(current -> current > 0 ? current - 1 : 0);
    }

    public int getCurrentOnlineUsers() {
        return onlineUsers.get();
    }
}
