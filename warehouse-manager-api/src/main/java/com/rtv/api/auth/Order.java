package com.rtv.api.auth;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Tanvi on 14/08/17.
 */
public class Order {

    public enum OrderType {
        SALE,
        PURCHASE
    }

    private String id;

    private String productID;
    private Product product;

    private String thirdPartyID;
    private ThirdParty thirdParty;

    private String batchID;
    private Batch batch;

    @NotNull
    private OrderType orderType;

    @NotNull
    @Email
    private String userEmail;

    @NotNull
    private Date date;

    @NotNull
    private Float price; //of individual product

    @NotNull
    private Float gst;

    @NotNull
    private Float totalCost;

    @NotNull
    private Integer quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getThirdPartyID() {
        return thirdPartyID;
    }

    public void setThirdPartyID(String thirdPartyID) {
        this.thirdPartyID = thirdPartyID;
    }

    public ThirdParty getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(ThirdParty thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getBatchID() {
        return batchID;
    }

    public void setBatchID(String batchID) {
        this.batchID = batchID;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
