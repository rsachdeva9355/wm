package com.rtv.api.auth;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Tanvi on 14/08/17.
 */
public class Product {

    private String id;

    @NotBlank
    private String companyProductName;

    @NotBlank
    private String company;

    @NotBlank
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyProductName() {
        return companyProductName;
    }

    public void setCompanyProductName(String companyProductName) {
        this.companyProductName = companyProductName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
