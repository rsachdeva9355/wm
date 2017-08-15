package com.rtv.store;

import com.rtv.api.auth.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import static com.rtv.util.Transformer.transform;

/**
 * Created by Tanvi on 15/08/17.
 */
public class UserDAO {
    private static Datastore store;

    public static void initUserDAO(Datastore stor) {
        store = stor;
    }

    public static User getUserByID(String userID) {
        UserDO userDO = store.createQuery(UserDO.class).field("id").equal(userID).get();
        return transform(userDO);
    }

    public static User getUserByEmailOrMobile(String emailOrMobile) {
        Query<UserDO> query = store.createQuery(UserDO.class);
        query.or(
                query.criteria("email").equal(emailOrMobile),
                query.criteria("mobile").equal(emailOrMobile)
                );
        return transform(query.get());
    }
}
