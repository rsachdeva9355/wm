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

@Entity(value = "order", noClassnameStored = true)
@Indexes({
        @Index(
                fields = {
                        @Field("productID")
                }
        ),
        @Index(
                fields = {
                        @Field("batchID")
                }
        )}
)
public class OrderDO extends PersistedEntity {
    @Id
    private String id;
    private String productID;
    private String batchID;
    private Float price; //of individual product
    private Float sgst;
    private Float cgst;
    private Float totalCost;
    private Integer quantity;

    public OrderDO() {
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

    public String getBatchID() {
        return batchID;
    }

    public void setBatchID(String batchID) {
        this.batchID = batchID;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getSgst() {
        return sgst;
    }

    public void setSgst(Float sgst) {
        this.sgst = sgst;
    }

    public Float getCgst() {
        return cgst;
    }

    public void setCgst(Float cgst) {
        this.cgst = cgst;
    }

    public Float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Float totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
