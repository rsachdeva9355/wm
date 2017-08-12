package com.rtv.store;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * Created by Tanvi on 09/08/17.
 */

@Entity(value = "product", noClassnameStored = true)
public class ProductDO extends PersistedEntity {
    @Id
    private String id;

    @Property
    private String companyProductName;

    @Property
    private String companyProduct;

    @Property
    private String productName;
}
