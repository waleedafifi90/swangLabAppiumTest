package com.swanglabs.app.tests;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private static final String USERNAME = "test-Username";
    private static final String PASSWORD = "test-Password";
    private static final String LOGIN    = "test-LOGIN";
    private static final String STANDARD_USER_TEXT = "standard_user";

    private final By usernameField = AppiumBy.accessibilityId(USERNAME);
    private final By passwordField = AppiumBy.accessibilityId(PASSWORD);
    private final By loginButton   = AppiumBy.accessibilityId(LOGIN);

    @Test
    public void validLogin_works() throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));
    
        actions.scrollIntoViewByText(STANDARD_USER_TEXT);
        WebElement el = driver.findElement(
                AppiumBy.xpath("//android.widget.TextView[@text=\"" + STANDARD_USER_TEXT + "\"]")
            );
        Thread.sleep(2000);
        actions.tryClick(el);
       
        actions.recoverIfBackgrounded();

        WebElement userEl = actions.scrollIntoViewByDesc(USERNAME);
        actions.clickReliable(userEl);

        actions.ensureFilled(usernameField, "standard_user");
        actions.ensureFilled(passwordField, "secret_sauce");

        actions.assertHasText(usernameField, "Username field should have text");
        actions.assertHasText(passwordField, "Password field should have text");

        driver.findElement(loginButton).click();

        By cart = AppiumBy.accessibilityId("test-Cart");
        By productsTitle = By.xpath("//*[@text='PRODUCTS']");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(cart),
                ExpectedConditions.visibilityOfElementLocated(productsTitle)
        ));
    }
}
