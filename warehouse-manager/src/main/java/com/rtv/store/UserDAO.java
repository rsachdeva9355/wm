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
        return transform(queryUserByID(userID));
    }

    public static UserDO getUserDOByID(String userID) {
        return queryUserByID(userID);
    }

    public static User getUserByUsername(String username) {
        return transform(queryUserByUsername(username));
    }

    public static UserDO getUserDOByUsername(String username) {
        return queryUserByUsername(username);
    }

//    public static User getUserByEmailOrMobile(String emailOrMobile) {
//        return transform(queryUserByEmailOrMobile(emailOrMobile));
//    }

//    public static UserDO getUserDOByEmailOrMobile(String emailOrMobile) {
//        return queryUserByEmailOrMobile(emailOrMobile);
//    }

    public static UserDO queryUserByUsername(String username) {
        Query<UserDO> query = store.createQuery(UserDO.class).filter("username", username);
        return query.get();
    }

//    private static UserDO queryUserByEmailOrMobile(String emailOrMobile) {
//        Query<UserDO> query = store.createQuery(UserDO.class);
//        query.or(
//            query.criteria("email").equal(emailOrMobile),
//            query.criteria("mobile").equal(emailOrMobile)
//        );
//        return query.get();
//    }

    private static UserDO queryUserByID(String userID) {
        Query<UserDO> query = store.createQuery(UserDO.class).field("id").equal(userID);
        return query.get();
    }
}
