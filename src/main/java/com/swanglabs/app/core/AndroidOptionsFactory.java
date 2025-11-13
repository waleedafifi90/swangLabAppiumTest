package com.swanglabs.app.core;

import io.appium.java_client.android.options.UiAutomator2Options;

import java.nio.file.Path;
import java.time.Duration;

public final class AndroidOptionsFactory {

    private AndroidOptionsFactory() {
    }

    public static UiAutomator2Options fromConfig() {
        ConfigLoader config = ConfigLoader.getInstance();

        UiAutomator2Options options = new UiAutomator2Options();

        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");

        options.setDeviceName(config.get("android.deviceName"));
        String platformVersion = config.getOptional("android.platformVersion");
        if (platformVersion != null && !platformVersion.isBlank()) {
            options.setPlatformVersion(platformVersion);
        }

        String appPath = config.getOptional("android.appPath");
        if (appPath != null && !appPath.isBlank()) {
            String absoluteAppPath = Path.of(appPath).toAbsolutePath().toString();
            options.setApp(absoluteAppPath);
        }

        String appPackage = config.getOptional("android.appPackage");
        if (appPackage != null && !appPackage.isBlank()) {
            options.setAppPackage(appPackage);
        }

        String appActivity = config.getOptional("android.appActivity");
        if (appActivity != null && !appActivity.isBlank()) {
            options.setAppActivity(appActivity);
        }

        String udid = config.getOptional("android.udid");
        if (udid != null && !udid.isBlank()) {
            options.setUdid(udid);
        }

        String newCommandTimeoutKey = "timeouts.newCommandSeconds";
        String nct = config.getOptional(newCommandTimeoutKey);
        if (nct != null && !nct.isBlank()) {
            options.setNewCommandTimeout(Duration.ofSeconds(
                    Long.parseLong(nct)));
        }

        return options;
    }
}
