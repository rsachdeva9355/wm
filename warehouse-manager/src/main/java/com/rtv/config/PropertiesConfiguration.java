package com.rtv.config;

import com.meltmedia.dropwizard.mongo.MongoConfiguration;
import com.rtv.WarehouseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Startup configuration of nosql properties for WarehouseManager.
 *
 * @author rachit
 */
public class PropertiesConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesConfiguration.class);
    private static final String HOST_PORT_DELIMETER = ":";
    private static final String SERVER_CONFIG_DELIMETER = ",";

    public static final String NOSQL_PROPERTIES = "nosql.properties";

    public static final String MONGO_SEEDS_CONFIG = "warehousemanager_mongo_seeds_config";
    public static final String MONGO_DATABASE = "warehousemanager_mongo_database_name";

    private Properties prop = new Properties();

    @NotNull
    @Valid
    private MongoConfiguration mongo;

    private static PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();

    private PropertiesConfiguration() {
        loadProperties();
        setProperties();
    }

    public static PropertiesConfiguration getInstance() {
        return propertiesConfiguration;
    }

    public MongoConfiguration getMongoConfiguration() {
        return mongo;
    }

    public void setMongoConfiguration(MongoConfiguration mongoConfiguration) {
        this.mongo = mongoConfiguration;
    }

    private void loadProperties() {
        ClassLoader loader = WarehouseManager.class.getClassLoader();
        try (InputStream inputStreamNoSql = loader.getResourceAsStream(NOSQL_PROPERTIES))
        {
            if (inputStreamNoSql != null) {
                prop.load(inputStreamNoSql);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    private void setProperties() {
        MongoConfiguration mongo = new MongoConfiguration();
        mongo.setDatabase(prop.getProperty(MONGO_DATABASE));
        mongo.setSeeds(getSeeds());
        setMongoConfiguration(mongo);

    }

    private List<MongoConfiguration.Server> getSeeds() {
        String serverConfig = prop.getProperty(MONGO_SEEDS_CONFIG);
        List<MongoConfiguration.Server> seeds = prepareMongoServers(serverConfig);
        return seeds;
    }

    private List<MongoConfiguration.Server> prepareMongoServers(String serverConfig) {
        if (!serverConfig.contains(SERVER_CONFIG_DELIMETER)) {
            return Collections.singletonList(prepareServer(serverConfig));
        }
        List<MongoConfiguration.Server> servers = new ArrayList<>();
        String[] configs = serverConfig.split(SERVER_CONFIG_DELIMETER);
        for (String config : configs) {
            servers.add(prepareServer(config));
        }
        return servers;
    }

    private MongoConfiguration.Server prepareServer(String serverConfig) {
        if (!serverConfig.contains(HOST_PORT_DELIMETER)) {
            throw new IllegalArgumentException("Server config should be of the format" +
                                                   "host" + HOST_PORT_DELIMETER + "port." +
                                                   "Unable to parse " + serverConfig);
        }
        String[] hostPort = serverConfig.split(HOST_PORT_DELIMETER);
        MongoConfiguration.Server server = new MongoConfiguration.Server();
        server.setHost(hostPort[0]);
        server.setPort(Integer.parseInt(hostPort[1]));
        return server;
    }
}