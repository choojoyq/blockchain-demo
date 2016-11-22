package com.altoros.blockchain.demo.security;

import com.altoros.blockchain.demo.model.User;

/**
 * @author Nikita Gorbachevski
 */
public class AuthenticatedUserHolder {

    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    private AuthenticatedUserHolder() {
    }

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    public static User getUser() {
        return userThreadLocal.get();
    }
}
