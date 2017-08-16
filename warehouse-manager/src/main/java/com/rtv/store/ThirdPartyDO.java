package com.rtv.store;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

import com.rtv.api.auth.ThirdParty;

/**
 * Created by Tanvi on 09/08/17.
 */
@Entity(value = "thirdparty", noClassnameStored = true)
@Indexes(
        @Index(
                fields = {
                        @Field("name")
                },
                unique = true
        )
)
public class ThirdPartyDO extends PersistedEntity {

    @Id
    private String id;
    private String name;
    private ThirdParty.ThirdPartyType type;

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
}
