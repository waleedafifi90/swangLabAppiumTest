package com.swanglabs.app.tests;

import com.swanglabs.app.core.ConfigLoader;
import com.swanglabs.app.core.DriverManager;
import com.swanglabs.app.utils.MobileActions;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseTest {

    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected MobileActions actions;


    private static final Path ARTIFACTS_DIR = Path.of("target", "artifacts");
    private int implicitSeconds = 5;
    private int explicitSeconds = 5;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        Files.createDirectories(ARTIFACTS_DIR);

        ConfigLoader cfg = ConfigLoader.getInstance();
        String imp = cfg.getOptional("timeouts.implicitSeconds");
        String exp = cfg.getOptional("timeouts.explicitSeconds");
        if (imp != null && !imp.isBlank()) implicitSeconds = Integer.parseInt(imp);
        if (exp != null && !exp.isBlank()) explicitSeconds = Integer.parseInt(exp);

        DriverManager.createDriver();
        driver = DriverManager.getDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitSeconds));
        wait = new WebDriverWait(driver, Duration.ofSeconds(explicitSeconds));
        
        String appPackage = cfg.getOptional("android.appPackage");
        actions = new MobileActions(driver, wait, appPackage);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
            DriverManager.quitDriver();
    }
}
