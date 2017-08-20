package com.rtv.store;

import com.rtv.api.auth.Bill;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;
import java.util.List;

/**
 * Created by Tanvi on 19/08/17.
 */
@Entity(value = "bill", noClassnameStored = true)

public class BillDO {
    @Id
    private String id;
    private String userID;
    private Bill.BillType billType;
    private List<String> orderIDs;
    private String billNumber;
    private Date date;
    private String thirdPartyID;
    private boolean discarded;

    public BillDO() {
        this.id = new ObjectId().toString();
    }

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

    public Bill.BillType getBillType() {
        return billType;
    }

    public void setBillType(Bill.BillType billType) {
        this.billType = billType;
    }

    public List<String> getOrderIDs() {
        return orderIDs;
    }

    public void setOrderIDs(List<String> orderIDs) {
        this.orderIDs = orderIDs;
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

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }
}
