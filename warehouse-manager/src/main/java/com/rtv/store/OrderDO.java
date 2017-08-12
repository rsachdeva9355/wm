package com.rtv.store;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import com.rtv.Constants;

/**
 * Created by Tanvi on 09/08/17.
 */

@Entity(value = "order", noClassnameStored = true)
public class OrderDO extends PersistedEntity {
    @Id
    private String id;

    @Property
    private String orderType;

    @Property
    private String userID;

    @Property
    private Long date;

    @Property
    private Long thirdPartyID;

    @Property
    private String productID;

    @Property
    private String batchID;

    @Property
    private Float costPrice; //of individual product

    @Property
    private Float gst;

    @Property
    private Float totalCost;

    @Property
    private Integer quantity;

    private OrderDO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Constants.OrderType getOrderType() {
        return Constants.OrderType.valueOf(orderType);
    }

    public void setOrderType(Constants.OrderType orderType) {
        this.orderType = orderType.name();
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getThirdPartyID() {
        return thirdPartyID;
    }

    public void setThirdPartyID(Long thirdPartyID) {
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

    public Float getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Float costPrice) {
        this.costPrice = costPrice;
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
