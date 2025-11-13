package com.swanglabs.app.tests;

import com.swanglabs.app.core.ConfigLoader;
import com.swanglabs.app.core.DriverManager;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
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

    private static final Path ARTIFACTS_DIR = Path.of("target", "artifacts");
    private int implicitSeconds = 10;
    private int explicitSeconds = 20;

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
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (!result.isSuccess()) {
                saveFailureScreenshot(result);
            }
        } catch (Exception e) {
            System.err.println("Failed saving artifacts: " + e.getMessage());
        } finally {
            DriverManager.quitDriver();
        }
    }

    private void saveFailureScreenshot(ITestResult result) throws Exception {
        if (!(driver instanceof TakesScreenshot)) return;
        byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

        String fileName = sanitize(result.getMethod().getMethodName())
                + "_" + timestamp() + ".png";
        Path out = ARTIFACTS_DIR.resolve(fileName);
        Files.write(out, png);
        System.out.println("Saved screenshot: " + out.toAbsolutePath());
    }

    private static String timestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    private static String sanitize(String s) {
        return s == null ? "screenshot"
                : s.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
