package com.rtv.store;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;

/**
 * Address data object.
 * 
 * @author bhupi
 */
@Entity("something")
@Indexes({
    @Index(
        options = @IndexOptions(unique=false),
        fields = {
            @Field(value = "index")
        }
    )
})
public class DemoDO {

    @Id
    private String id;
}
