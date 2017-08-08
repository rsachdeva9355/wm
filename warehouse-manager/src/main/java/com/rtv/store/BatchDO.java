package com.rtv.store;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.Date;

/**
 * Created by Tanvi on 09/08/17.
 */

@Entity(value = "inventory", noClassnameStored = true)
public class BatchDO {
    @Id
    private String id;

    @Property
    private String productID;

    @Property
    private String batchNum;

    @Property
    private Date mfgDate;

    @Property
    private Date expDate;

}
