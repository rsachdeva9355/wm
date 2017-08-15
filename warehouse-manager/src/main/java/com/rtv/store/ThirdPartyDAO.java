package com.rtv.store;

import com.rtv.api.auth.ThirdParty;
import org.mongodb.morphia.Datastore;

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
        ThirdPartyDO thirdPartyDO = store.createQuery(ThirdPartyDO.class).field("id").equal(thirdPartyID).get();
        return transform(thirdPartyDO);
    }

    public static ThirdParty getThirdPartyByName(String thirdPartyName) {
        ThirdPartyDO thirdPartyDO = store.createQuery(ThirdPartyDO.class).field("name").equal(thirdPartyName).get();
        return transform(thirdPartyDO);
    }

}
