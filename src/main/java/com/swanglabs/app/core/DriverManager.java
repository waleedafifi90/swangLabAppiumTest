package com.swanglabs.app.core;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public final class DriverManager {

    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void createDriver() {
        if (driver.get() != null) {
            return;
        }

        ConfigLoader config = ConfigLoader.getInstance();
        PlatformType platform = PlatformType.valueOf(
                config.get("platform").toUpperCase()
        );

        try {
            AppiumDriver appiumDriver;

            switch (platform) {
                case ANDROID -> {
                    boolean useLocalService =
                            Boolean.parseBoolean(config.getOptional("appium.useLocalService"));

                    URL serverUrl;
                    if (useLocalService) {
                        AppiumServerManager.startServer();
                        serverUrl = AppiumServerManager.getServerUrl();
                    } else {
                        serverUrl = new URL(config.get("appium.serverUrl"));
                    }

                    UiAutomator2Options options = AndroidOptionsFactory.fromConfig();
                    appiumDriver = new AndroidDriver(serverUrl, options);
                }

                default -> throw new UnsupportedOperationException(
                        "Platform not supported yet: " + platform);
            }

            int implicitWaitSeconds = config.getInt("timeouts.implicitSeconds");
            appiumDriver
                    .manage()
                    .timeouts()
                    .implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));

            driver.set(appiumDriver);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL", e);
        }
    }

    public static AppiumDriver getDriver() {
        AppiumDriver current = driver.get();
        if (current == null) {
            throw new IllegalStateException(
                    "Driver is not initialized for this thread. " +
                    "Call DriverManager.createDriver() first."
            );
        }
        return current;
    }

    public static void quitDriver() {
        AppiumDriver current = driver.get();
        if (current != null) {
            current.quit();
            driver.remove();
        }
    }
}
