package com.rtv.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtv.Constants;
import com.rtv.models.Order;
import com.rtv.store.OrderDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Tanvi on 09/08/17.
 */

@Path("order")
@Api(value = "order")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static final Logger LOG = LoggerFactory.getLogger(OrderResource.class);

    private final Datastore store;
    private ObjectMapper objectMapper;

    public OrderResource(
            final Datastore store, ObjectMapper objectMapper)
    {
        this.store = store;
        this.objectMapper = objectMapper;
    }

    @POST
    @Timed
    @ApiOperation(value = "Create a new order")
    @Produces(MediaType.APPLICATION_JSON)
    public @Valid
    Order create(
            @Valid
            @NotNull
            @ApiParam("order to be created")
            Order order
                )
    {
        OrderDO orderDO;
        validateOrders(order);
        //TODO - check for duplicate orders
        orderDO = new OrderDO();
        orderDO.setId(new ObjectId().toString());
        orderDO.setOrderType(Constants.OrderType.valueOf(order.getOrderType()));    //can avoid this - kept for validation purposes. Or can keep it in the validation code
        orderDO.setUserID(order.getUserID());
        orderDO.setDate(order.getDate());
        orderDO.setThirdPartyID(order.getThirdPartyID());
        orderDO.setProductID(order.getProductID());
        orderDO.setBatchID(order.getBatchID());
        orderDO.setCostPrice(order.getCostPrice());
        orderDO.setGst(order.getGst());
        orderDO.setTotalCost(order.getTotalCost());
        orderDO.setQuantity(order.setQuantity());
        return order;
    }

    public void validateOrders(Order order) {
        if (order.getUserID() == null) {
            //TODO - user authentication
        }
        if (order.getBatchID() == null) {
            throwWhatError();
        }
        if (order.getCostPrice() == null) {
            throwWhatError();
        }
        if (order.getDate() == null) {
            throwWhatError();
        }
        if (order.getGst() == null) {
            throwWhatError();
        }
        if (order.getOrderType() == null) {
            throwWhatError();
        }
        if (order.getProductID() == null) {
            throwWhatError();
        }
        if (order.getQuantity() == null) {
            throwWhatError();
        }
        if (order.getThirdPartyID() == null) {
            throwWhatError();
        }
        if (order.getTotalCost() == null) {
            throwWhatError();
        }
    }

    public void throwWhatError() {}
}
