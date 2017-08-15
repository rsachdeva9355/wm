package com.rtv.store;

import org.mongodb.morphia.annotations.PrePersist;

import java.util.Date;

public class PersistedEntity {

    private Date created;
    private Date updated;

    @PrePersist
    private void prePersist() {
        if (null == created) {
            created = new Date();
        }
        updated = new Date();
    }


    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }
}
