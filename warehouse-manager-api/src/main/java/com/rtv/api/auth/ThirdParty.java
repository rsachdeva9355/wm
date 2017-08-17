package com.rtv.api.auth;

/**
 * Created by Tanvi on 15/08/17.
 */
public class ThirdParty {

    public enum ThirdPartyType {
        SELLER,
        BUYER
    }

    private String id;

    private String name;

    private ThirdPartyType type;

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
}
