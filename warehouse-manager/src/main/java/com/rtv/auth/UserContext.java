package com.rtv.auth;

import com.rtv.api.auth.User;

public class UserContext {

    private static ThreadLocal<UserContext> ctx= new ThreadLocal<>();

    private User user;

    private UserContext() {}

    public static UserContext current() {
        UserContext userContext = ctx.get();
        if (userContext == null) {
            userContext = new UserContext();
            ctx.set(userContext);
        }
        return userContext;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
