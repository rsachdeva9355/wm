package com.rtv.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtv.models.Order;
import com.rtv.store.OrderDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
        return order;
    }

    public void validateOrders(Order order) {

    }
}
