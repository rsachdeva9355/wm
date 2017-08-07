package com.rtv;

import com.codahale.metrics.SharedMetricRegistries;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.meltmedia.dropwizard.mongo.MongoBundle;
import com.rtv.config.WarehouseManagerConfiguration;
import com.rtv.store.DemoDO;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Master of addresses and other geographical structures in the OLP ecosystem.
 *  
 * @author bhupi
 */
public class WarehouseManager extends Application<WarehouseManagerConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(WarehouseManager.class);

    private MongoBundle<WarehouseManagerConfiguration> mongoBundle;

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

        final Morphia morphia = new Morphia();
        morphia.map(DemoDO.class);
        Datastore store = morphia.createDatastore(mongoBundle.getClient(), mongoBundle.getDB().getName());
        store.ensureIndexes();

        DemoResource demoResource;
        demoResource = new DemoResource(store, environment.getObjectMapper());
        environment.jersey().register(demoResource);
        LOG.info("Registered de o resource..");
    }
}
