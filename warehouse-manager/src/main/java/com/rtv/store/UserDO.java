package com.rtv.store;

import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

import com.rtv.api.auth.User;

@Entity(value = "user", noClassnameStored = true)
@Indexes({
        @Index(
                fields = {
                        @Field("email"),
                        @Field("mobile")
                },
                unique = true
        ),
        @Index(
                fields = {
                        @Field("username")
                },
                unique = true
        )
})
public class UserDO extends PersistedEntity {

    public UserDO() {
        this.id = new ObjectId().toString();
    }

    public UserDO(User user) {
        id = new ObjectId().toString();
        name = user.getName();
        email = user.getEmail();
        mobile = user.getMobile();
        password = DigestUtils.md5Hex(mobile + "{" + email + "}");
    }

    @Id
    private String id;
    private String name;
    private String email;
    private String mobile;
    private String password;
    private String username;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
