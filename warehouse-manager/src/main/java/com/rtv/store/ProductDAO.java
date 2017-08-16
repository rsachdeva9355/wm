package com.rtv.store;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.rtv.api.auth.Product;

import static com.rtv.util.Transformer.transform;
import static com.rtv.util.Transformer.transformProductDOs;

/**
 * Created by Tanvi on 15/08/17.
 */
public class ProductDAO {

    private static Datastore store;

    public static void initProductDAO(Datastore stor) {
        store = stor;
    }

    public static Product getProductByID(String productID) {
        ProductDO productDO = store.createQuery(ProductDO.class).field("id").equal(productID).get();
        return transform(productDO);
    }

    /**
     * Both product name and company-specific name
     */
    public static List<Product> getProductsByName(String productName) {
        Query<ProductDO> query = store.createQuery(ProductDO.class);
        query.or(
                query.criteria("name").equal(productName),
                query.criteria("companyProductName").equal(productName)
                );
        return transformProductDOs(query.asList());
    }

    /**
     * Both product name and company-specific name
     */
    public static List<String> getProductIDsForName(String productName) {
        Query<ProductDO> query = store.createQuery(ProductDO.class);
        query.or(query.criteria("name").equal(productName), query.criteria("companyProductName").equal(productName));
        List<ProductDO> productDOsWithIDOnly = query.retrievedFields(true, "id").asList();
        List<String> ids = new ArrayList<>(productDOsWithIDOnly.size());
        for (ProductDO productDO : productDOsWithIDOnly) {
            ids.add(productDO.getId());
        }
        return ids;
    }
}
