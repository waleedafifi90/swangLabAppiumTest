package com.swanglabs.app.core;

import io.appium.java_client.service.local.AppiumDriverLocalService;

import java.net.URL;

public final class AppiumServerManager {

    private static AppiumDriverLocalService service;

    private AppiumServerManager() {
    }

    public static void startServer() {
        if (service == null) {
            service = AppiumDriverLocalService.buildDefaultService();
            service.start();

            if (!service.isRunning()) {
                throw new IllegalStateException(
                        "Appium server did not start correctly.");
            }
        }
    }

    public static URL getServerUrl() {
        if (service == null || !service.isRunning()) {
            throw new IllegalStateException(
                    "Appium server is not running. Call startServer() first.");
        }
        return service.getUrl();
    }

    public static void stopServer() {
        if (service != null && service.isRunning()) {
            service.stop();
        }
    }
}
