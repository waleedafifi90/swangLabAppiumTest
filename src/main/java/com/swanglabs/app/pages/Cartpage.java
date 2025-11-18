package com.swanglabs.app.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.android.AndroidDriver;

public class Cartpage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    private By cartItem = By.xpath("//android.view.ViewGroup[@content-desc=\"test-Item\"]");
    private By backButton = By.xpath("//android.view.ViewGroup[@content-desc=\"test-CONTINUE SHOPPING\"]");
    private By removeButton = By.xpath("(//android.view.ViewGroup[@content-desc=\"test-REMOVE\"])[1]");
    private By checkoutButton = By.xpath("//android.view.ViewGroup[@content-desc=\"test-CHECKOUT\"]");

    public Cartpage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isItemDisplayed() {
        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(cartItem));
        return item.isDisplayed();
    }

    public void goBackToProducts() {
        WebElement backBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(backButton));
        backBtn.click();
    }
    public void removeFromCart() {
        WebElement removeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(removeButton));
        removeBtn.click();
    }
    public void goToCheckout() {
        WebElement checkoutBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutButton));
        checkoutBtn.click();
    }
}
