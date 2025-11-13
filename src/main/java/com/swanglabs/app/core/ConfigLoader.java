package com.swanglabs.app.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigLoader {

    private static final String CONFIG_FILE = "config.properties";
    private static volatile ConfigLoader instance;
    private final Properties properties = new Properties();

    private ConfigLoader() {
        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new IllegalStateException(
                        "Could not find " + CONFIG_FILE + " on the classpath");
            }

            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load configuration from " + CONFIG_FILE, e);
        }
    }

    public static ConfigLoader getInstance() {
        if (instance == null) {
            synchronized (ConfigLoader.class) {
                if (instance == null) {
                    instance = new ConfigLoader();
                }
            }
        }
        return instance;
    }

    public String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException(
                    "Property '" + key + "' is not set in " + CONFIG_FILE);
        }
        return value.trim();
    }

    public String getOptional(String key) {
        String value = properties.getProperty(key);
        return value != null ? value.trim() : null;
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
