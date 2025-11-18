package com.swanglabs.app.pages;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.appium.java_client.android.AndroidDriver;

public class Productpage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    private By addToCartBtn = By.xpath("(//android.view.ViewGroup[@content-desc=\"test-ADD TO CART\"])[1]");
    private By cartIcon = By.xpath("//android.view.ViewGroup[@content-desc=\"test-Cart\"]");
    private By removeButton = By.xpath("//android.view.ViewGroup[@content-desc=\"test-REMOVE\"]");
    private By number = By.xpath("//android.view.ViewGroup[@content-desc='test-Cart']//android.widget.TextView");


    public Productpage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void addItemToCart() {
        WebElement addBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartBtn));
        addBtn.click();
    }

    public void openCart() {
        WebElement cart = wait.until(ExpectedConditions.visibilityOfElementLocated(cartIcon));
        cart.click();
    }

    public void removeItemFromCart() {
        WebElement removeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(removeButton));
        removeBtn.click();
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

public void verfieAddMultiple () {
    WebElement numberOfItems = wait.until(ExpectedConditions.visibilityOfElementLocated(number));
    String count = numberOfItems.getText();

    Assert.assertEquals(count, "3", "Cart badge count is NOT 3!");
}


}
