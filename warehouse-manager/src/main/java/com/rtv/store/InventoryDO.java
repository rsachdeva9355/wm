package com.rtv.store;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

/**
 * Created by Tanvi on 09/08/17.
 */

@Entity(value = "inventory", noClassnameStored = true)
@Indexes(
        @Index(
                fields = {
                        @Field("productID")
                },
                unique = true
        )
)
public class InventoryDO extends PersistedEntity {
    @Id
    private String id;
    private String productID;
    private Integer quantity;

    public InventoryDO() {
        this.id = new ObjectId().toString();
    }

    public String getId() {
        return id;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
