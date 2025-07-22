package com.helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private final Properties properties;

    public ConfigReader() {
        properties = new Properties();
        String pathToProperties = "src/test/resources/config.properties";

        loadProperties(pathToProperties);
    }

    private void loadProperties(String filePath) {
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public String getAuthToken() {
        return properties.getProperty("auth.token");
    }

    public String getEndpointPutUser() {
        return properties.getProperty("endpoint.putUser");
    }

    public String getEndpointGetUser() {
        return properties.getProperty("endpoint.getUser");
    }

    public String getEndpointPostUser() {
        return properties.getProperty("endpoint.postUser");
    }
}