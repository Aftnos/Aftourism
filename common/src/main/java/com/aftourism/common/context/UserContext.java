package com.aftourism.common.context;

/**
 * Thread local holder for current user identifier.
 */
public final class UserContext {

    private static final ThreadLocal<String> USERNAME_HOLDER = new ThreadLocal<>();

    private UserContext() {
    }

    public static void setUsername(String username) {
        USERNAME_HOLDER.set(username);
    }

    public static String getUsername() {
        return USERNAME_HOLDER.get();
    }

    public static void clear() {
        USERNAME_HOLDER.remove();
    }
}
