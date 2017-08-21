package com.rtv.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtv.api.auth.Batch;
import com.rtv.api.auth.Bill;
import com.rtv.api.auth.Order;
import com.rtv.api.auth.Product;
import com.rtv.api.auth.ThirdParty;
import com.rtv.api.auth.User;
import com.rtv.store.BatchDAO;
import com.rtv.store.BatchDO;
import com.rtv.store.BillDAO;
import com.rtv.store.BillDO;
import com.rtv.store.OrderDAO;
import com.rtv.store.OrderDO;
import com.rtv.store.ProductDAO;
import com.rtv.store.ProductDO;
import com.rtv.store.ThirdPartyDAO;
import com.rtv.store.ThirdPartyDO;
import com.rtv.store.UserDAO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import static com.rtv.util.Transformer.transform;
import static com.rtv.util.Transformer.transformBillDOs;

/**
 * Created by Tanvi on 19/08/17.
 */

@Path("bills")
@Api(value = "Bill Management")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BillResource {
    private static final Logger LOG = LoggerFactory.getLogger(OrderResource.class);

    private final Datastore store;
    private ObjectMapper objectMapper;

    public BillResource(
            final Datastore store, ObjectMapper objectMapper)
    {
        this.store = store;
        this.objectMapper = objectMapper;
    }

    @POST
    @ApiOperation(value = "Create a new bill")
    @Produces(MediaType.APPLICATION_JSON)
    public @Valid
    Bill create(
            @Valid
            @NotNull
            @ApiParam("bill to be created")
            Bill bill
                )
    {
        User user = UserDAO.getUserByUsername(bill.getUsername());
        if (null == user) {
            throw new BadRequestException("User does not exist for username " + bill.getUsername());
        }

        BillDO billDO = new BillDO();
        billDO.setUserID(user.getId());
        billDO.setBillNumber(bill.getBillNumber());
        billDO.setDate(bill.getDate());

        ThirdParty thirdParty = bill.getThirdParty();
        if (null == thirdParty) {
            //this should have a thirdpartyID
            String thirdPartyID = bill.getThirdPartyID();
            if (null == thirdPartyID) {
                throw new BadRequestException("No third party or third party id");
            }
            //check if this id exists
            ThirdPartyDO tp = ThirdPartyDAO.getThirdPartyDOByID(thirdPartyID);
            if (null == tp) {
                throw new BadRequestException("Third Party with id {" + thirdPartyID  + "} does not exist");
            }
            billDO.setThirdPartyID(thirdPartyID);
            //thirdParty = transform(tp); - not needed
        } else {
            ThirdPartyDO thirdPartyDO = new ThirdPartyDO();
            thirdPartyDO.setName(thirdParty.getName());
            thirdPartyDO.setType(thirdParty.getType());
            store.save(thirdPartyDO);
            thirdParty.setId(thirdPartyDO.getId());
            billDO.setThirdPartyID(thirdPartyDO.getId());
        }

        //Create individual orders and get their ids to store in a list in the bill
        List<Order> orders = bill.getOrders();
        List<String> orderIDs = new ArrayList<>(orders.size());
        for (Order order : orders) {
            String orderId = persistOrder(order).getId();
            order.setId(orderId);
            orderIDs.add(orderId);
        }

        billDO.setOrderIDs(orderIDs);
        billDO.setBillType(bill.getBillType());
        store.save(billDO);
        bill.setId(billDO.getId());
        return bill;
    }

    private OrderDO persistOrder(Order order) {
        OrderDO orderDO = new OrderDO();
        order.setOrderType(order.getOrderType());

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

        orderDO.setPrice(order.getPrice());
        orderDO.setCgst(order.getCgst());
        orderDO.setSgst(order.getSgst());
        orderDO.setQuantity(order.getQuantity());
        orderDO.setTotalCost(order.getTotalCost());
        store.save(orderDO);
        return orderDO;
    }

    @GET
    @ApiOperation(value = "Get bills")
    @Produces(MediaType.APPLICATION_JSON)
    public @Valid
    List<Bill> get(@QueryParam("username") String username,
                    @QueryParam("exactdate") Date exactDate,
                    @QueryParam("startdate") Date startDate,
                    @QueryParam("enddate") Date endDate,
                    @QueryParam("thirdpartyname") String thirdPartyName,
//                    @QueryParam("productname") String productName,
//                    @QueryParam("batchcode") String batchCode,
                    @QueryParam("billtype") Bill.BillType billType)
    {
        Query<BillDO> query = store.createQuery(BillDO.class).filter("discarded", false);
        List<Criteria> criterion = new ArrayList<>();

        if (StringUtils.isNotBlank(username)) {
            User user = UserDAO.getUserByUsername(username);
            if (null == user) {
                throw new NotFoundException("User does not exist for username " + username);
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

        if (null != billType) {
            criterion.add(query.criteria("billType").equal(billType));
        }
        if (criterion.size() > 0) {
            query.and(criterion.toArray(new Criteria[criterion.size()]));
        }

        return transformBillDOs(query.asList());
    }

    @POST
    @Path("update")
    @ApiOperation(value = "Update a bill. Username on the bill cannot be updated")
    @Produces(MediaType.APPLICATION_JSON)
    public @Valid
    Bill update(
            @Valid
            @NotNull
            @ApiParam("bill to be updated")
            Bill bill
                     )
    {
        String id = bill.getId();
        if (null == id) {
            throw new BadRequestException("Cannot delete a bill without an id");
        }
        BillDO billDO = BillDAO.getBillDOByID(id);
        if (null == billDO) {
            throw new BadRequestException("Bill with id " + id + " does not exist");
        }
        if (billDO.isDiscarded()) {
            throw new BadRequestException("Bill with id " + id + " no longer exists");
        }

        billDO.setBillNumber(bill.getBillNumber());
        billDO.setDate(bill.getDate());

        ThirdParty thirdParty = bill.getThirdParty();
        if (null == thirdParty) {
            //this should have a thirdpartyID
            String thirdPartyID = bill.getThirdPartyID();
            if (null == thirdPartyID) {
                throw new BadRequestException("No third party or third party id");
            }
            //check if this id exists
            ThirdPartyDO tp = ThirdPartyDAO.getThirdPartyDOByID(thirdPartyID);
            if (null == tp) {
                throw new BadRequestException("Third Party with id {" + thirdPartyID  + "} does not exist");
            }
            billDO.setThirdPartyID(thirdPartyID);
            //thirdParty = transform(tp); - not needed
        } else {
            ThirdPartyDO thirdPartyDO = new ThirdPartyDO();
            thirdPartyDO.setName(thirdParty.getName());
            thirdPartyDO.setType(thirdParty.getType());
            store.save(thirdPartyDO);
            thirdParty.setId(thirdPartyDO.getId());
            billDO.setThirdPartyID(thirdPartyDO.getId());
        }

        //if an order has id, update that order, else create new order and add it to this bill
        //This will also change the sorting order of the orderIDs
        List<Order> orders = bill.getOrders();
        List<String> newOrderIDs = new ArrayList<>(orders.size());
        for (Order order : orders) {
            String orderID = order.getId();
            if (null == orderID) {
                orderID = persistOrder(order).getId();
            }
            else {
                //find this order and persist its new details
                OrderDO orderDO = OrderDAO.getOrderDOByID(orderID);
                if (null == orderDO) {
                    throw new BadRequestException("Order with orderID " + orderID + " does not exist");
                }
                orderID = updateOrder(orderDO, order).getId();
            }
            order.setId(orderID);
            newOrderIDs.add(orderID);
        }

        billDO.setOrderIDs(newOrderIDs);
        billDO.setBillType(bill.getBillType());
        store.save(billDO);
        bill.setId(billDO.getId());
        return bill;
    }

    private OrderDO updateOrder(OrderDO orderDO, Order order) {
        Product product = order.getProduct();
        if (null == product) {
            //this should have a product id
            String productID = order.getProductID();
            if (null == productID) {
                throw new BadRequestException("No product or product id in order " + order.getId());
            }
            //check if this id exists
            ProductDO p = ProductDAO.getProductDOByID(productID);
            if (null == p) {
                throw new BadRequestException(
                        "Product with id {" + productID + "} does not exist in order " + order.getId()
                );
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

        orderDO.setPrice(order.getPrice());
        orderDO.setCgst(order.getCgst());
        orderDO.setSgst(order.getSgst());
        orderDO.setQuantity(order.getQuantity());
        orderDO.setTotalCost(order.getTotalCost());
        store.save(orderDO);
        return orderDO;
    }

    @DELETE
    @ApiOperation(value = "Delete a bill")
    @Produces(MediaType.APPLICATION_JSON)
    public
    void delete(@Valid
                @NotNull
                @ApiParam("bill to be deleted")
                Bill bill
               )
    {
        String id = bill.getId();
        if (null == id) {
            throw new BadRequestException("Cannot delete a bill without an id");
        }
        BillDO billDO = BillDAO.getBillDOByID(id);
        if (null == billDO) {
            throw new BadRequestException("Bill with id " + id + " does not exist");
        }
        if (billDO.isDiscarded()) {
            throw new BadRequestException("Bill with id " + id + " has already been deleted");
        }
        billDO.setDiscarded(false);
        store.save(billDO);
    }
}
