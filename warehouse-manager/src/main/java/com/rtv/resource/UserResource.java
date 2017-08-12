package com.rtv.resource;


import javax.validation.Valid;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotBlank;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DuplicateKeyException;
import com.rtv.api.auth.User;
import com.rtv.store.UserDO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import static com.rtv.util.Transformer.transform;
import static java.text.MessageFormat.format;
import static javax.ws.rs.core.Response.Status.CONFLICT;

@Path("users")
@Api(value = "User Management")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final Datastore store;

    public UserResource(Datastore store) {
        this.store = store;
    }

    @POST
    @ApiOperation("Creates a new user")
    public @Valid User create(@Valid User user) {
        log.debug("Create user request for {}", user.getEmail());
        UserDO userDO = new UserDO(user);
        try {
            store.save(userDO);
        } catch (DuplicateKeyException e) {
            String errorMessage =
                format("User with email/mobile [{0}/{1}] already exists", user.getEmail(), user.getMobile());
            log.error(errorMessage);
            throw new ClientErrorException(errorMessage, CONFLICT);
        }
        user.setId(userDO.getId());
        return user;
    }

    @GET
    @ApiOperation("Get existing user")
    public @Valid User get(@NotBlank @QueryParam("username") String emailOrMobile) {
        User user = getUserByEmailOrMobile(emailOrMobile);
        if (null == user) {
            throw new NotFoundException("User does not exist. Invalid username");
        }
        return user;
    }

    private User getUserByEmailOrMobile(String emailOrMobile) {
        UserDO userDO = store.createQuery(UserDO.class).field("email").equal(emailOrMobile).get();
        if (null == userDO) {
            userDO = store.createQuery(UserDO.class).field("mobile").equal(emailOrMobile).get();
        }
        return transform(userDO);
    }
}
