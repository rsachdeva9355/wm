package com.rtv.store;

import com.rtv.Constants;
import org.mongodb.morphia.annotations.*;

/**
 * Created by Tanvi on 09/08/17.
 */

@Entity(value = "order", noClassnameStored = true)
public class OrderDO {
    @Id
    private String id;

    @Property
    private String orderType;

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

}
