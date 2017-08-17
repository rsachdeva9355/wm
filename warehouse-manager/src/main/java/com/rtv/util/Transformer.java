package com.rtv.util;

import java.util.ArrayList;
import java.util.List;

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
import com.rtv.store.UserDO;

public class Transformer {

    public static User transform(UserDO userDO) {
        if (null == userDO) {
            return null;
        }
        User user = new User();
        user.setId(userDO.getId());
        user.setName(userDO.getName());
        user.setEmail(userDO.getEmail());
        user.setMobile(userDO.getMobile());
        return user;
    }

    public static List<User> transformUserDOs(List<UserDO> userDOs) {
        if (null == userDOs) {
            return null;
        }
        List<User> list = new ArrayList<>(userDOs.size());
        for (UserDO userDO : userDOs) {
            list.add(transform(userDO));
        }
        return list;
    }

    public static Product transform(ProductDO productDO) {
        if (null == productDO) {
            return null;
        }
        Product product = new Product();
        product.setId(productDO.getName());
        product.setCompany(productDO.getCompany());
        product.setCompanyProductName(productDO.getCompanyProductName());
        product.setName(productDO.getName());
        return product;
    }

    public static List<Product> transformProductDOs(List<ProductDO> productDOs) {
        if (null == productDOs) {
            return null;
        }
        List<Product> list = new ArrayList<>(productDOs.size());
        for (ProductDO productDO : productDOs) {
            list.add(transform(productDO));
        }
        return list;
    }

    public static ThirdParty transform(ThirdPartyDO thirdPartyDO) {
        if (null == thirdPartyDO) {
            return null;
        }
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setId(thirdPartyDO.getId());
        thirdParty.setName(thirdPartyDO.getName());
        thirdParty.setType(thirdPartyDO.getType());
        return thirdParty;
    }

    public static List<ThirdParty> transformThirdPartyDOs(List<ThirdPartyDO> thirdPartyDOs) {
        if (null == thirdPartyDOs) {
            return null;
        }
        List<ThirdParty> list = new ArrayList<>(thirdPartyDOs.size());
        for (ThirdPartyDO thirdPartyDO : thirdPartyDOs) {
            list.add(transform(thirdPartyDO));
        }
        return list;
    }

    public static Batch transform(BatchDO batchDO) {
        if (null == batchDO) {
            return null;
        }
        Batch batch = new Batch();
        batch.setId(batchDO.getId());
        batch.setCode(batchDO.getCode());
        batch.setExpDate(batchDO.getExpDate());
        batch.setMfgDate(batchDO.getMfgDate());
        batch.setPack(batchDO.getPack());
        return batch;
    }

    public static List<Batch> transformBatchDOs(List<BatchDO> batchDOs) {
        if (null == batchDOs) {
            return null;
        }
        List<Batch> list = new ArrayList<>(batchDOs.size());
        for (BatchDO batchDO : batchDOs) {
            list.add(transform(batchDO));
        }
        return list;
    }

    public static Order transform(OrderDO orderDO) {
        if (null == orderDO) {
            return null;
        }
        Order order = new Order();
        order.setId(orderDO.getId());
        order.setProduct(ProductDAO.getProductByID(orderDO.getProductID()));
        order.setBatch(BatchDAO.getBatchByID(orderDO.getBatchID()));
        order.setThirdParty(ThirdPartyDAO.getThirdPartyByID(orderDO.getThirdPartyID()));
        order.setPrice(orderDO.getPrice());
        order.setDate(orderDO.getDate());
        order.setGst(orderDO.getGst());
        order.setOrderType(orderDO.getOrderType());
        order.setQuantity(orderDO.getQuantity());
        order.setTotalCost(orderDO.getTotalCost());
        order.setUserEmail(UserDAO.getUserByID(orderDO.getUserID()).getEmail());
        return order;
    }

    public static List<Order> transformOrderDOs(List<OrderDO> orderDOs) {
        if (null == orderDOs) {
            return null;
        }
        List<Order> list = new ArrayList<>(orderDOs.size());
        for (OrderDO orderDO : orderDOs) {
            list.add(transform(orderDO));
        }
        return list;
    }


}
