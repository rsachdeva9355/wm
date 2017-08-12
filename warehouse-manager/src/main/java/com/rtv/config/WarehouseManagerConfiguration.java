package com.rtv.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meltmedia.dropwizard.mongo.MongoConfiguration;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class WarehouseManagerConfiguration extends Configuration {

    @JsonProperty("swagger")
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    @JsonProperty("signingKey")
    private String signingKey;

    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }

    public MongoConfiguration getMongo() {
        return PropertiesConfiguration.getInstance().getMongoConfiguration();
    }

    public String getSigningKey() {
        return signingKey;
    }
}