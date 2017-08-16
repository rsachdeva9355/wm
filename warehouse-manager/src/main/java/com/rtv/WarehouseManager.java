package com.rtv;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtContext;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.SharedMetricRegistries;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Optional;
import com.meltmedia.dropwizard.mongo.MongoBundle;
import com.rtv.api.auth.User;
import com.rtv.auth.UserContext;
import com.rtv.auth.filter.JwtFilter;
import com.rtv.auth.filter.NoAuthFilter;
import com.rtv.config.WarehouseManagerConfiguration;
import com.rtv.resource.AuthResource;
import com.rtv.resource.OrderResource;
import com.rtv.resource.UserResource;
import com.rtv.store.BatchDAO;
import com.rtv.store.OrderDAO;
import com.rtv.store.ProductDAO;
import com.rtv.store.ThirdPartyDAO;
import com.rtv.store.UserDAO;

import io.dropwizard.Application;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class WarehouseManager extends Application<WarehouseManagerConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(WarehouseManager.class);

    private MongoBundle<WarehouseManagerConfiguration> mongoBundle;
    private static String[] authenticatedUrls = {"/users*", "/orders*"};

    public static void main(String[] args) throws Exception {
        new WarehouseManager().run(args);
    }

    @Override
    public String getName() {
        return "location-master";
    }

    @Override
    public void initialize(Bootstrap<WarehouseManagerConfiguration> bootstrap) {
        bootstrap.setMetricRegistry(SharedMetricRegistries.getOrCreate("default"));
        
        ObjectMapper mapper = bootstrap.getObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        bootstrap.addBundle(new SwaggerBundle<WarehouseManagerConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(
                WarehouseManagerConfiguration configuration)
            {
                return configuration.getSwaggerBundleConfiguration();
            }
        });

        mongoBundle = MongoBundle.<WarehouseManagerConfiguration>builder()
            .withConfiguration(WarehouseManagerConfiguration::getMongo).build();
        bootstrap.addBundle(mongoBundle);
    }

    @Override
    public void run(WarehouseManagerConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().setUrlPattern("/*");

        final FilterRegistration.Dynamic cors =
            environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        final Morphia morphia = new Morphia();
        morphia.mapPackage("com.rtv.store");
        Datastore store = morphia.createDatastore(mongoBundle.getClient(), mongoBundle.getDB().getName());
        store.ensureIndexes();

        //Register DAO
        OrderDAO.initOrderDAO(store);
        ProductDAO.initProductDAO(store);
        ThirdPartyDAO.initThirdPartyDAO(store);
        BatchDAO.initBatchDAO(store);
        UserDAO.initUserDAO(store);

        // Register Resources
        OrderResource orderResource = new OrderResource(store, environment.getObjectMapper());
        environment.jersey().register(orderResource);

        UserResource userResource = new UserResource();
        environment.jersey().register(userResource);

        AuthResource authResource = new AuthResource();
        environment.jersey().register(authResource);

        JwtFilter jwtFilter = new JwtFilter("kukky");
        environment.servlets().addFilter("JwtFilter", jwtFilter)
            .addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST), true, authenticatedUrls
            );

        environment.servlets().addFilter("NoAuthFilter", new NoAuthFilter())
            .addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST), true, authenticatedUrls
            );
    }

    private static class ExampleAuthenticator implements Authenticator<JwtContext, User> {

            @Override
            public Optional<User> authenticate(JwtContext context) {
                // Provide your own implementation to lookup users based on the principal attribute in the
                // JWT Token. E.g.: lookup users from a database etc.
                // This method will be called once the token's signature has been verified

                // In case you want to verify different parts of the token you can do that here.
                // E.g.: Verifying that the provided token has not expired.

                // All JsonWebTokenExceptions will result in a 401 Unauthorized response.

                try {
                    final String subject = context.getJwtClaims().getSubject();
                    if ("rsachdev".equals(subject)) {
                        User user = new User();
                        user.setEmail("rsachdev");
                        user.setMobile("8588875531");
                        UserContext.current().setUser(user);
                        return Optional.of(user);
                    }
                    return Optional.absent();
                }
                catch (MalformedClaimException e) { return Optional.absent(); }
        }
    }
}
