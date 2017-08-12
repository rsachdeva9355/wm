package com.rtv.store;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * Created by Tanvi on 09/08/17.
 */

@Entity(value = "inventory", noClassnameStored = true)
public class BatchDO extends PersistedEntity {
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
