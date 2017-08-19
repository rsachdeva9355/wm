package com.rtv.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtv.api.auth.Batch;
import com.rtv.api.auth.Order;
import com.rtv.api.auth.Product;
import com.rtv.api.auth.ThirdParty;
import com.rtv.api.auth.User;
import com.rtv.store.BatchDAO;
import com.rtv.store.BatchDO;
import com.rtv.store.OrderDO;
import com.rtv.store.ProductDAO;
import com.rtv.store.ProductDO;
import com.rtv.store.ThirdPartyDAO;
import com.rtv.store.ThirdPartyDO;
import com.rtv.store.UserDAO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.rtv.util.Transformer.transform;
import static com.rtv.util.Transformer.transformOrderDOs;

/**
 * Created by Tanvi on 09/08/17.
 */

@Path("orders")
@Api(value = "Order Management")
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
        OrderDO orderDO = new OrderDO();
        User user = UserDAO.getUserByEmailOrMobile(order.getUserEmail());
        if (null == user) {
            throw new BadRequestException("User does not exist for username " + order.getUserEmail());
        }
        orderDO.setUserID(user.getId());

        Product product = order.getProduct();
        if (null == product) {
            //this should have a product id
            String productID = order.getProductID();
            if (null == productID) {
                throw new BadRequestException("No product or product id");
            }
            //check if this id exists
            ProductDO p = ProductDAO.getProductDOByID(productID);
            if (null == p) {
                throw new BadRequestException("Product with id {" + productID + "} does not exist");
            }
            orderDO.setProductID(productID);
            product = transform(p);
        } else {
            ProductDO productDO = new ProductDO();
            productDO.setCompany(product.getCompany());
            productDO.setGenericName(product.getGenericName());
            productDO.setName(product.getName());
            store.save(productDO);
            product.setId(productDO.getId());
            orderDO.setProductID(productDO.getId());
        }

        Batch batch = order.getBatch();
        if (null == batch) {
            //this should have a batch id
            String batchID = order.getBatchID();
            if (null == batchID) {
                throw new BadRequestException("No batch or batch id");
            }
            //check if this id exists
            BatchDO b = BatchDAO.getBatchDOByID(batchID);
            if (null == b) {
                throw new BadRequestException("Batch with id {" + batchID + "} does not exist");
            }
            orderDO.setBatchID(batchID);
            //batch = transform(b); - not needed
        } else {
            BatchDO batchDO = new BatchDO();
            batchDO.setProductID(product.getId());
            batchDO.setCode(batch.getCode());
            batchDO.setExpDate(batch.getExpDate());
            batchDO.setMfgDate(batch.getMfgDate());
            batchDO.setPack(batch.getPack());
            store.save(batchDO);
            batch.setId(batchDO.getId());
            orderDO.setBatchID(batchDO.getId());
        }

        ThirdParty thirdParty = order.getThirdParty();
        if (null == thirdParty) {
            //this should have a thirdpartyID
            String thirdPartyID = order.getThirdPartyID();
            if (null == thirdPartyID) {
                throw new BadRequestException("No third party or third party id");
            }
            //check if this id exists
            ThirdPartyDO tp = ThirdPartyDAO.getThirdPartyDOByID(thirdPartyID);
            if (null == tp) {
                throw new BadRequestException("Third Party with id {" + thirdPartyID  + "} does not exist");
            }
            orderDO.setThirdPartyID(thirdPartyID);
            //thirdParty = transform(tp); - not needed
        } else {
            ThirdPartyDO thirdPartyDO = new ThirdPartyDO();
            thirdPartyDO.setName(thirdParty.getName());
            thirdPartyDO.setType(thirdParty.getType());
            store.save(thirdPartyDO);
            thirdParty.setId(thirdPartyDO.getId());
            orderDO.setThirdPartyID(thirdPartyDO.getId());
        }

        orderDO.setPrice(order.getPrice());
        orderDO.setDate(new Date());
        orderDO.setGst(order.getGst());
        orderDO.setOrderType(order.getOrderType());
        orderDO.setQuantity(order.getQuantity());
        orderDO.setTotalCost(order.getTotalCost());
        store.save(orderDO);
        return order;
    }

    @GET
    @ApiOperation(value = "Get orders by id")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public @Valid
    Order getByID(@PathParam("id") String id)
    {
        Query<OrderDO> query = store.createQuery(OrderDO.class);
        OrderDO orderDO = query.filter("id", id).get();
        return transform(orderDO);
    }

    @GET
    @ApiOperation(value = "Get orders")
    @Produces(MediaType.APPLICATION_JSON)
    public @Valid
    List<Order> get(@QueryParam("username") String username,
                    @QueryParam("exactdate") Date exactDate,
                    @QueryParam("startdate") Date startDate,
                    @QueryParam("enddate") Date endDate,
                    @QueryParam("thirdpartyname") String thirdPartyName,
                    @QueryParam("productname") String productName,
                    @QueryParam("batchcode") String batchCode,
                    @QueryParam("ordertype") Order.OrderType orderType)
    {
        Query<OrderDO> query = store.createQuery(OrderDO.class);
        List<Criteria> criterion = new ArrayList<>();
        if (StringUtils.isNotBlank(username)) {
            User user = UserDAO.getUserByEmailOrMobile(username);
            if (null == user) {
                throw new NotFoundException("User does not exist for emailorMobile " + username);
            }
            criterion.add(query.criteria("userID").equal(user.getId()));
        }
        if (exactDate != null) {
            criterion.add(query.criteria("date").equal(exactDate));
            if (startDate != null || endDate != null) {
                throw new BadRequestException("Cannot send start/end date with exact date");
            }
        }
        if (startDate != null) {
            criterion.add(query.criteria("date").greaterThan(startDate));
        }
        if (endDate != null) {
            criterion.add(query.criteria("date").lessThan(endDate));
        }
        if (StringUtils.isNotBlank(thirdPartyName)) {
            ThirdParty thirdParty = ThirdPartyDAO.getThirdPartyByName(thirdPartyName);
            if (thirdParty != null) {
                criterion.add(query.criteria("thirdPartyID").equal(thirdParty.getId()));
            }
        }
        if (StringUtils.isNotBlank(productName)) {
            List<String> productIDs = ProductDAO.getProductIDsForName(productName);
            if (productIDs != null && productIDs.size() > 0) {
                criterion.add(query.criteria("productID in").equal(productIDs));
            }
        }
        if (StringUtils.isNotBlank(batchCode)) {
            Batch batch = BatchDAO.getBatchByCode(batchCode);
            if (batch != null) {
                criterion.add(query.criteria("batchID").equal(batch.getId()));
            }
        }
        if (null != orderType) {
            criterion.add(query.criteria("orderType").equal(orderType));
        }
        if (criterion.size() > 0) {
            query.and(criterion.toArray(new Criteria[criterion.size()]));
        }
        return transformOrderDOs(query.asList());
    }

}
