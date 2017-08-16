package com.rtv.store;

import org.mongodb.morphia.Datastore;

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
        BatchDO batchDO = store.createQuery(BatchDO.class).field("id").equal(batchID).get();
        return transform(batchDO);
    }

    public static Batch getBatchByCode(String batchCode) {
        BatchDO batchDO = store.createQuery(BatchDO.class).field("code").equal(batchCode).get();
        return transform(batchDO);
    }

}
