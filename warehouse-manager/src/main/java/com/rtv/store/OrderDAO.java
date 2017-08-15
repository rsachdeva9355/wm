package com.rtv.store;

import com.rtv.api.auth.Batch;
import com.rtv.api.auth.Order;
import com.rtv.api.auth.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import javax.ws.rs.NotFoundException;
import java.util.Date;
import java.util.List;

import static com.rtv.util.Transformer.transformOrderDOs;

/**
 * Created by Tanvi on 15/08/17.
 *
 * TODO - Remove this - useless class
 */
public class OrderDAO {
    private static Datastore store;

    public static void initOrderDAO(Datastore stor) {
        store = stor;
    }

    public static List<Order> getOrdersByUserID(String userID) {
        List<OrderDO> orderDOs = store.createQuery(OrderDO.class).field("userID").equal(userID).asList();
        return transformOrderDOs(orderDOs);
    }

    public static List<Order> getOrdersByEmailOrMobile(String emailOrMobile) throws NotFoundException {
        User user = UserDAO.getUserByEmailOrMobile(emailOrMobile);
        if (null == user) {
            throw new NotFoundException("User does not exist for emailorMobile " + emailOrMobile);
        }
        List<OrderDO> orderDOs = store.createQuery(OrderDO.class).field("userID").equal(user.getId()).asList();
        return transformOrderDOs(orderDOs);
    }

    public static List<Order> getOrdersByDate(Date date) {
        List<OrderDO> orderDOs = store.createQuery(OrderDO.class).field("date").equal(date).asList();
        return transformOrderDOs(orderDOs);
    }

    public static List<Order> getOrderByDateRange(Date startDate, Date endDate) {
        Query<OrderDO> query = store.createQuery(OrderDO.class);
        query.and(
                query.criteria("date").lessThan(endDate),
                query.criteria("date").greaterThan(startDate));
        return transformOrderDOs(query.asList());
    }

    public static List<Order> getOrdersByThirdPartyID(String thirdPartyID) {
        List<OrderDO> orderDOs = store.createQuery(OrderDO.class).field("thirdPartyID").equal(thirdPartyID).asList();
        return transformOrderDOs(orderDOs);
    }

    /**
     * Both product name and company specific product name
     */
    public static List<Order> getOrdersByProductName(String productName) {
        List<String> productIDs = ProductDAO.getProductIDsForName(productName);
        return transformOrderDOs(store.createQuery(OrderDO.class).filter("productID in", productIDs).asList());
    }

    public static List<Order> getOrdersByBatchCode(String batchCode) {
        Batch batch = BatchDAO.getBatchByCode(batchCode);
        return transformOrderDOs(store.createQuery(OrderDO.class).filter("batchID", batch).asList());
    }
}
