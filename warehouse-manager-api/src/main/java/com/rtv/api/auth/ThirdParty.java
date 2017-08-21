package com.rtv.api.auth;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Tanvi on 15/08/17.
 */
public class ThirdParty {

    public enum ThirdPartyType {
        SELLER,
        BUYER
    }

    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private ThirdPartyType type;

    private String phonenumber;
    private String gstTIN;
    private String licenseNumber;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ThirdPartyType getType() {
        return type;
    }

    public void setType(ThirdPartyType type) {
        this.type = type;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getGstTIN() {
        return gstTIN;
    }

    public void setGstTIN(String gstTIN) {
        this.gstTIN = gstTIN;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
