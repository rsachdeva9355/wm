package com.rtv.store;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.rtv.api.auth.Batch;

import static com.rtv.util.Transformer.transform;

/**
 * Created by Tanvi on 15/08/17.
 */
public class BatchDAO {

    private static Datastore store;

    public static void initBatchDAO(Datastore stor) {
        store = stor;
    }

    public static Batch getBatchByID(String batchID) {
        return transform(queryBatchByID(batchID));
    }

    public static BatchDO getBatchDOByID(String batchID) {
        return queryBatchByID(batchID);
    }

    private static BatchDO queryBatchByID(String batchID) {
        Query<BatchDO> query = store.createQuery(BatchDO.class).field("id").equal(batchID);
        return query.get();
    }

    public static Batch getBatchByCode(String batchCode) {
        return transform(queryBatchByCode(batchCode));
    }

    public static BatchDO getBatchDOByCode(String batchCode) {
        return queryBatchByCode(batchCode);
    }

    private static BatchDO queryBatchByCode(String batchCode) {
        Query<BatchDO> query = store.createQuery(BatchDO.class).field("code").equal(batchCode);
        return query.get();
    }

    public static List<BatchDO> searchByCode(String query) {
        return store.createQuery(BatchDO.class).search(query).asList();
    }

}
