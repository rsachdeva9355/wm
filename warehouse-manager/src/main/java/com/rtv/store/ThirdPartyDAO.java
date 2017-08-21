package com.rtv.store;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.rtv.api.auth.ThirdParty;

import static com.rtv.util.Transformer.transform;

/**
 * Created by Tanvi on 15/08/17.
 */
public class ThirdPartyDAO {

    private static Datastore store;

    public static void initThirdPartyDAO(Datastore stor) {
        store = stor;
    }

    public static ThirdParty getThirdPartyByID(String thirdPartyID) {
        return transform(queryThirdPartyByID(thirdPartyID));
    }

    public static ThirdParty getThirdPartyByName(String thirdPartyName) {
        return transform(queryThirdPartyByName(thirdPartyName));
    }

    public static ThirdPartyDO getThirdPartyDOByID(String thirdPartyID) {
        return queryThirdPartyByID(thirdPartyID);
    }

    private static ThirdPartyDO queryThirdPartyByID(String thirdPartyID) {
        Query<ThirdPartyDO> query = store.createQuery(ThirdPartyDO.class).field("id").equal(thirdPartyID);
        return query.get();
    }

    private static ThirdPartyDO queryThirdPartyByName(String thirdPartyName) {
        Query<ThirdPartyDO> query = store.createQuery(ThirdPartyDO.class).field("name").equal(thirdPartyName);
        return query.get();
    }

    public static List<ThirdPartyDO> searchByCode(String query) {
        return store.createQuery(ThirdPartyDO.class).search(query).asList();
    }
}
