package com.rtv.store;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.rtv.api.auth.User;

import static com.rtv.util.Transformer.transform;

/**
 * Created by Tanvi on 15/08/17.
 */
public class UserDAO {
    private static Datastore store;

    public static void initUserDAO(Datastore stor) {
        store = stor;
    }

    public static void save(UserDO userDO) {
        store.save(userDO);
    }

    public static User getUserByID(String userID) {
        UserDO userDO = store.createQuery(UserDO.class).field("id").equal(userID).get();
        return transform(userDO);
    }

    public static User getUserByEmailOrMobile(String emailOrMobile) {
        return transform(queryUserByEmailOrMobile(emailOrMobile));
    }

    public static UserDO getUserDOByEmailOrMobile(String emailOrMobile) {
        return queryUserByEmailOrMobile(emailOrMobile);
    }

    private static UserDO queryUserByEmailOrMobile(String emailOrMobile) {
        Query<UserDO> query = store.createQuery(UserDO.class);
        query.or(
            query.criteria("email").equal(emailOrMobile),
            query.criteria("mobile").equal(emailOrMobile)
        );
        return query.get();
    }
}
