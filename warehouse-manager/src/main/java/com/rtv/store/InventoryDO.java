package com.rtv.store;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * Created by admin on 09/08/17.
 */

@Entity(value = "inventory", noClassnameStored = true)
public class InventoryDO {
    @Id
    private String id;

    @Property
    private String productID;

    @Property
    private Integer quantity;

}
