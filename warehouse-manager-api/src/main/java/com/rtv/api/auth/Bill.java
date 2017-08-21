package com.rtv.api.auth;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Tanvi on 19/08/17.
 *
 * A bill contains many orders and third party information
 */
public class Bill {

    public enum BillType {
        SALE,
        PURCHASE
    }

    private String id;

    private String userID;

    @NotBlank
    private String username;

    @NotNull
    private BillType billType;

    @NotEmpty
    private List<Order> orders;

    @NotNull
    private String billNumber;

    @NotNull
    private Date date;

    private String thirdPartyID;
    private ThirdParty thirdParty;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BillType getBillType() {
        return billType;
    }

    public void setBillType(BillType billType) {
        this.billType = billType;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
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

    public ThirdParty getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(ThirdParty thirdParty) {
        this.thirdParty = thirdParty;
    }
}
