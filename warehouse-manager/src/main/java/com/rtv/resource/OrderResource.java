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
            throw new BadRequestException("User does not exist");
        }
        orderDO.setUserID(user.getId());

        Product product = order.getProduct();
        if (product.getId() == null) {
            ProductDO productDO = new ProductDO();
            productDO.setCompany(product.getCompany());
            productDO.setCompanyProductName(product.getCompanyProductName());
            productDO.setName(product.getName());
            store.save(productDO);
            product.setId(productDO.getId());
            orderDO.setProductID(productDO.getId());
        } else {
            //check if this id exists
            Product p = ProductDAO.getProductByID(product.getId());
            if (p == null) {
                throw new BadRequestException("Product with id " + product.getId() + " does not exist");
            }
            orderDO.setProductID(product.getId());
        }

        Batch batch = order.getBatch();
        if (batch.getId() == null) {
            BatchDO batchDO = new BatchDO();
            batchDO.setProductID(product.getId());
            batchDO.setCode(batch.getCode());
            batchDO.setExpDate(batch.getExpDate());
            batchDO.setMfgDate(batch.getMfgDate());
            batchDO.setPack(batch.getPack());
            store.save(batchDO);
            batch.setId(batchDO.getId());
            orderDO.setBatchID(batchDO.getId());
        } else {
            //check if this id exists
            Batch b = BatchDAO.getBatchByID(batch.getId());
            if (b == null) {
                throw new BadRequestException("Batch with id " + batch.getId() + " does not exist");
            }
            orderDO.setBatchID(batch.getId());
        }

        ThirdParty thirdParty = order.getThirdParty();
        if (thirdParty.getId() == null) {
            ThirdPartyDO thirdPartyDO = new ThirdPartyDO();
            thirdPartyDO.setName(thirdParty.getName());
            thirdPartyDO.setType(thirdParty.getType());
            store.save(thirdPartyDO);
            thirdParty.setId(thirdPartyDO.getId());
            orderDO.setThirdPartyID(thirdPartyDO.getId());
        } else {
            //check if this exists
            ThirdParty t = ThirdPartyDAO.getThirdPartyByID(thirdParty.getId());
            if (t == null) {
                throw new BadRequestException("Third party with id " + thirdParty.getId() + " does not exist");
            }
            orderDO.setThirdPartyID(thirdParty.getId());
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
                    @QueryParam("batchcode") String batchCode)
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
        if (criterion.size() > 0) {
            query.and(criterion.toArray(new Criteria[criterion.size()]));
        }

        return transformOrderDOs(query.asList());
    }

}
