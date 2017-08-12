package com.rtv.auth;

import com.rtv.api.auth.User;

public class UserContext {

    private static UserContext userContext = null;

    private User user;

    private UserContext() {}

    public static UserContext current() {
        if (null == userContext) {
            userContext = new UserContext();
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
