package com.rtv.store;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.utils.IndexType;

import com.rtv.api.auth.ThirdParty;

/**
 * Created by Tanvi on 09/08/17.
 */
@Entity(value = "thirdparty", noClassnameStored = true)
@Indexes(
        @Index(
                fields = {
                        @Field(value = "name", type = IndexType.TEXT)
                },
                unique = true
        )
)
public class ThirdPartyDO extends PersistedEntity {

    @Id
    private String id;
    private String name;
    private ThirdParty.ThirdPartyType type;
    private String phonenumber;
    private String gstTIN;
    private String licenseNumber;
    private String address;

    public ThirdPartyDO() {
        this.id = new ObjectId().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ThirdParty.ThirdPartyType getType() {
        return type;
    }

    public void setType(ThirdParty.ThirdPartyType type) {
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
