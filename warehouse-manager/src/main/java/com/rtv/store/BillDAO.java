package com.rtv.store;

import com.rtv.api.auth.Bill;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import static com.rtv.util.Transformer.transform;

/**
 * Created by Tanvi on 20/08/17.
 */
public class BillDAO {
    private static Datastore store;

    public static void initBillDAO(Datastore stor) {
        store = stor;
    }

    public static Bill getBillByID(String billID) {
        return transform(queryBillByID(billID));
    }

    public static BillDO getBillDOByID(String billID) {
        return queryBillByID(billID);
    }

    private static BillDO queryBillByID(String billID) {
        Query<BillDO> query = store.createQuery(BillDO.class);
        return query.field("id").equal(billID).get();
    }
}
