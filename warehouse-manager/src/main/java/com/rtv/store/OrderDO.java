package com.rtv.store;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

import com.rtv.api.auth.Order;

/**
 * Created by Tanvi on 09/08/17.
 */

@Entity(value = "order", noClassnameStored = true)
@Indexes({
        @Index(
                fields = {
                        @Field("orderType")
                }
        ),
        @Index(
                fields = {
                        @Field("userID")
                }
        ),
        @Index(
                fields = {
                        @Field("thirdPartyID")
                }
        ),
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
    private Order.OrderType orderType;
    private String userID;
    private Date date;
    private String thirdPartyID;
    private String productID;
    private String batchID;
    private Float price; //of individual product
    private Float gst;
    private Float totalCost;
    private Integer quantity;

    public OrderDO() {
        this.id = new ObjectId().toString();
    }

    public String getId() {
        return id;
    }

    public Order.OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(Order.OrderType orderType) {
        this.orderType = orderType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getThirdPartyID() {
        return thirdPartyID;
    }

    public void setThirdPartyID(String thirdPartyID) {
        this.thirdPartyID = thirdPartyID;
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

    public Float getGst() {
        return gst;
    }

    public void setGst(Float gst) {
        this.gst = gst;
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
