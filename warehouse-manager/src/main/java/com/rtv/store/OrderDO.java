package com.rtv.store;

import com.rtv.Constants;
import org.mongodb.morphia.annotations.*;

/**
 * Created by Tanvi on 09/08/17.
 */

@Entity("Order")
public class OrderDO {
    @Id
    private String id;

    @Property
    private String orderID;

    @Property
    private String orderType;

    private OrderDO() {}

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Constants.OrderType getOrderType() {
        return Constants.OrderType.valueOf(orderType);
    }

    public void setOrderType(Constants.OrderType orderType) {
        this.orderType = orderType.name();
    }
}
