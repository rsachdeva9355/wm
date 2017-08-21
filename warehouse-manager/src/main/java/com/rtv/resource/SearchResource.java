package com.rtv.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotBlank;

import com.rtv.api.auth.Batch;
import com.rtv.api.auth.Bill;
import com.rtv.api.auth.Product;
import com.rtv.api.auth.ThirdParty;
import com.rtv.store.BatchDAO;
import com.rtv.store.BatchDO;
import com.rtv.store.BillDAO;
import com.rtv.store.BillDO;
import com.rtv.store.ProductDAO;
import com.rtv.store.ProductDO;
import com.rtv.store.ThirdPartyDAO;
import com.rtv.store.ThirdPartyDO;
import com.rtv.util.Transformer;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Path("search")
public class SearchResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResult search(@NotBlank @QueryParam("query") String query, @QueryParam("type") String type) {
        SearchResult searchResult = new SearchResult();
        if (isBlank(type) || "product".equals(type)) {
            List<ProductDO> products = ProductDAO.searchByCode(query);
            searchResult.setProducts(Transformer.transformProductDOs(products));
        }
        if (isBlank(type) || "batch".equals(type)) {
            List<BatchDO> batches = BatchDAO.searchByCode(query);
            searchResult.setBatches(Transformer.transformBatchDOs(batches));
        }
        if (isBlank(type) || "thirdParty".equals(type)) {
            List<ThirdPartyDO> thirdParties = ThirdPartyDAO.searchByCode(query);
            searchResult.setThirdParties(Transformer.transformThirdPartyDOs(thirdParties));
        }
        if (isBlank(type) || "bill".equals(type)) {
            List<BillDO> bills = BillDAO.searchByCode(query);
            searchResult.setBills(Transformer.transformBillDOs(bills));
        }
        return searchResult;
    }

    public static class SearchResult
    {
        private List<Product> products;
        private List<Batch> batches;
        private List<ThirdParty> thirdParties;
        private List<Bill> bills;

        public List<Product> getProducts() {
            return products;
        }

        public List<Batch> getBatches() {
            return batches;
        }

        public List<ThirdParty> getThirdParties() {
            return thirdParties;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public void setBatches(List<Batch> batches) {
            this.batches = batches;
        }

        public void setThirdParties(List<ThirdParty> thirdParties) {
            this.thirdParties = thirdParties;
        }

        public List<Bill> getBills() {
            return bills;
        }

        public void setBills(List<Bill> bills) {
            this.bills = bills;
        }
    }
}
