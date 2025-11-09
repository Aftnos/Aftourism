package com.aftourism.common.metrics;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Http session listener updating the online user counter.
 */
@Component
@RequiredArgsConstructor
public class SessionLifecycleListener implements HttpSessionListener {

    private final OnlineUserTracker onlineUserTracker;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        onlineUserTracker.increment();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        onlineUserTracker.decrement();
    }
}
