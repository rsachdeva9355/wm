package com.rtv.store;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * Created by Tanvi on 09/08/17.
 */
@Entity(value = "thirdparty", noClassnameStored = true)
public class ThirdPartyDO extends PersistedEntity {

    @Id
    private String id;

    @Property
    private String name;

    @Property
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
