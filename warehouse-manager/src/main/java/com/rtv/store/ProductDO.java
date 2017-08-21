package com.rtv.store;

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

@Entity(value = "product", noClassnameStored = true)
@Indexes({
        @Index(
                fields = {
                        @Field("name")
                }
        ),
        @Index(
                fields = {
                        @Field("genericName")
                }
        ),
        @Index(
            fields = {
                @Field(value = "name", type = IndexType.TEXT)
            }
        )
})
public class ProductDO extends PersistedEntity {
    @Id
    private String id;
    private String genericName;
    private String company;
    private String name;

    public ProductDO() {
        this.id = new ObjectId().toString();
    }

    public String getId() {
        return id;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
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
