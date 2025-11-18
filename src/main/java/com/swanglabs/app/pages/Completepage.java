package com.swanglabs.app.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;

public class Completepage {
    private AndroidDriver driver;
    private WebDriverWait wait;

    private By finishMessage = By.xpath("//android.widget.TextView[contains(@text,'THANK YOU')]");

    public Completepage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
    }

    public boolean isFinishMessageDisplayed() {
        try {
            WebElement finishMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(finishMessage));
            boolean visible = finishMsg.isDisplayed();
            System.out.println("Finish Message : " + visible);
            return visible;
        } catch (Exception e) {
            System.out.println("Finish message not found.");
            return false;
        }
    }
}
