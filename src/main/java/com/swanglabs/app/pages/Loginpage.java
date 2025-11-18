package com.swanglabs.app.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;

import java.time.Duration;

public class Loginpage {
    private AndroidDriver driver;
    private WebDriverWait wait;

    private By usernameField = By.xpath("//android.widget.EditText[@content-desc='test-Username']");
    private By passwordField = By.xpath("//android.widget.EditText[@content-desc='test-Password']");
    private By loginButton  = By.xpath("//android.view.ViewGroup[@content-desc='test-LOGIN']");
    private By errorMessage = By.xpath("//android.widget.TextView[@text=\"Username and password do not match any user in this service.\"]");

    public Loginpage(AndroidDriver driver) {
        this.driver = (AndroidDriver) driver; 
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void login(String username, String userPassword) {

        WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        usernameElement.click();
        usernameElement.sendKeys(username);

        WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordElement.click();
        passwordElement.sendKeys(userPassword);

        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginBtn.click();

    }

    public String getErrorMessage() {
    WebElement errorText = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
    return errorText.getText();
}

}
