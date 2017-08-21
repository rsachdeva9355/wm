package com.rtv.store;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.utils.IndexType;

/**
 * Created by Tanvi on 09/08/17.
 */

@Entity(value = "batch", noClassnameStored = true)
@Indexes({
        @Index(
                fields = {
                        @Field("productID")
                }
        ),
        @Index(
                fields = {
                        @Field("code")
                },
                unique = true

        ),
        @Index(
            fields = {
                @Field(value = "code", type = IndexType.TEXT)
            }
        )
})
public class BatchDO extends PersistedEntity {
    @Id
    private String id;
    private String productID;
    private String code;
    private Date mfgDate;
    private Date expDate;
    private String pack;

    public BatchDO() {
        this.id = new ObjectId().toString();
    }

    public String getId() {
        return id;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(Date mfgDate) {
        this.mfgDate = mfgDate;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }
}
