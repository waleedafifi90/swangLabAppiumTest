package com.swanglabs.app.pages;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;

public class Overviewpage {
    private AndroidDriver driver;
    private WebDriverWait wait;

    private By productName = By.xpath("//android.widget.TextView[contains(@text,'Sauce Labs')]");
    private By productPrice = By.xpath("(//android.widget.TextView[contains(@text,'$')])[1]");
    private By tax = By.xpath("(//android.widget.TextView[contains(@text,'$')])[3]");
    private By finalPrice = By.xpath("(//android.widget.TextView[contains(@text,'$')])[4]");
    private By finishButton = By.xpath("//android.view.ViewGroup[@content-desc=\"test-FINISH\"]");



    public Overviewpage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
    }

    public void Overview() throws InterruptedException {
        Thread.sleep(2000); // allow transition

        WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(productName));
        System.out.println("Product Name: " + name.getText());

        WebElement price = wait.until(ExpectedConditions.visibilityOfElementLocated(productPrice));
        System.out.println("Product Price: " + price.getText());

        WebElement taxAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(tax));
        System.out.println(taxAmount.getText());

        WebElement finalAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(finalPrice));
        System.out.println(finalAmount.getText());
    }

    public void finishOrder() {
        WebElement finishBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(finishButton));
        finishBtn.click();
    }

        public void scrollDown() {
    driver.executeScript("mobile: scrollGesture", Map.of(
        "left", 100,
        "top", 400,
        "width", 800,
        "height", 1200,
        "direction", "down",
        "percent", 0.30
    ));

    }
}

