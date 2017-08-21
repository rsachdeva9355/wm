package com.rtv.api.auth;

import javax.validation.constraints.NotNull;

/**
 * Created by Tanvi on 14/08/17.
 */
public class Order {

    private String id;

    private String productID;
    private Product product;

    private String batchID;
    private Batch batch;

    @NotNull
    private Float price; //of individual product

    @NotNull
    private Float sgst;

    @NotNull
    private Float cgst;

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
