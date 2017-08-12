package com.rtv.util;

import com.rtv.api.auth.User;
import com.rtv.store.UserDO;

public class Transformer {

    public static User transform(UserDO userDO) {
        User user = new User();
        user.setId(userDO.getId());
        user.setName(userDO.getName());
        user.setEmail(userDO.getEmail());
        user.setMobile(userDO.getMobile());
        return user;
    }
}
