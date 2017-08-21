package com.rtv.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtv.api.auth.User;
import com.rtv.auth.JwtHelper;
import com.rtv.store.UserDAO;
import com.rtv.store.UserDO;

import static com.rtv.auth.JwtHelper.TOKEN_EXPIRY_HOURS;
import static com.rtv.util.Transformer.transform;

@Path("/")
public class AuthResource {

    private static final Logger log = LoggerFactory.getLogger(AuthResource.class);
    private static final int AGE = TOKEN_EXPIRY_HOURS * 3600;

    public AuthResource() {
    }

    @POST
    @Path("authenticate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(
        @FormParam("username") @NotBlank String username,
        @FormParam("password") @NotBlank String password)
    {
        UserDO userDO = UserDAO.getUserDOByUsername(username);
        if (null != userDO) {
            String md5Password = DigestUtils.md5Hex(password + "{" + userDO.getUsername() + "}");
            if (!userDO.getPassword().equals(md5Password)) {
                return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_HTML).build();
            }
            User user = transform(userDO);
            String token = JwtHelper.issueToken(user);
            NewCookie jwtToken = new NewCookie("kukky", token);
            return Response.status(Response.Status.OK).cookie(jwtToken).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_HTML).build();
        }
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response logout()
    {
        NewCookie invalidate = new NewCookie("kukky", "expired", null, null, null, 0, false, false);
        return Response.status(Response.Status.OK).cookie(invalidate).build();
    }
}