package com.swanglabs.app.tests; // ← change to your package if needed

import com.swanglabs.app.core.ConfigLoader;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.HasOnScreenKeyboard;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;


public class LoginTest extends BaseTest {

    private static final String USERNAME_ELEMENT = "test-Username";
    private static final String PASSWORD_ELEMENT = "test-Password";
    private static final String LOGIN_BUTTON    = "test-LOGIN";
    private static final String STANDARD_USER_TEXT = "standard_user";

    private final By usernameField = AppiumBy.accessibilityId(USERNAME_ELEMENT);
    private final By passwordField = AppiumBy.accessibilityId(PASSWORD_ELEMENT);
    private final By loginButton   = AppiumBy.accessibilityId(LOGIN_BUTTON);


    private final String appPackage = ConfigLoader.getInstance().getOptional("android.appPackage");

    @Test
    public void validLogin_works() throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));

        clickStandardUserRobust();


        WebElement userEl = scrollIntoViewByDesc(USERNAME_ELEMENT);
        clickReliable(userEl);

        ensureFilled(usernameField, "standard_user");
        ensureFilled(passwordField, "secret_sauce");

        assertHasText(usernameField, "Username field should have text");
        assertHasText(passwordField, "Password field should have text");

        driver.findElement(loginButton).click();

        By cart = AppiumBy.accessibilityId("test-Cart");
        By productsTitle = By.xpath("//*[@text='PRODUCTS']");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(cart),
                ExpectedConditions.visibilityOfElementLocated(productsTitle)
        ));
    }


    private void clickStandardUserRobust() throws InterruptedException {
        maybeHideKeyboard();

        WebElement el = scrollIntoViewByText(STANDARD_USER_TEXT);

        Thread.sleep(2000);

        if (tryClick(el)) return;

        try {
            WebElement parent = driver.findElement(
                AppiumBy.xpath("//android.widget.TextView[@text=\"" + STANDARD_USER_TEXT + "\"]")
            );
            if (tryClick(parent)) return;
        } catch (NoSuchElementException ignored) {}

    }

    private WebElement scrollIntoViewByText(String text) {
        String ui = "new UiScrollable(new UiSelector().scrollable(true))"
                  + ".setAsVerticalList()"
                  + ".scrollIntoView(new UiSelector().text(\"" + text + "\"));";
        return driver.findElement(AppiumBy.androidUIAutomator(ui));
    }

    private WebElement scrollIntoViewByDesc(String desc) {
        String ui = "new UiScrollable(new UiSelector().scrollable(true))"
                  + ".setAsVerticalList()"
                  + ".scrollIntoView(new UiSelector().description(\"" + desc + "\"));";
        return driver.findElement(AppiumBy.androidUIAutomator(ui));
    }

    private boolean tryClick(WebElement el) {
        try {
        	WebElement element = driver.findElement(
                    AppiumBy.xpath("//android.widget.TextView[@text=\"" + STANDARD_USER_TEXT + "\"]")
                );
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            return true;
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            return false;
        }
    }

    private void clickReliable(WebElement el) {
        if (!tryClick(el)) {
            tapCenter(el);
        }
    }

    private void tapCenter(WebElement el) {
        Rectangle r = el.getRect();
        int x = r.getX() + r.getWidth() / 2;
        int y = r.getY() + r.getHeight() / 2;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(tap));
    }


    private void ensureFilled(By fieldLocator, String desiredValue) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldLocator));
        String current = safeGetTextOrAttr(field);
        if (current == null || current.isBlank()) {
            field.clear();
            field.sendKeys(desiredValue);
        }
    }

    private void assertHasText(By fieldLocator, String message) {
        WebElement field = driver.findElement(fieldLocator);
        String current = safeGetTextOrAttr(field);
        Assert.assertTrue(current != null && !current.isBlank(), message);
    }

    private String safeGetTextOrAttr(WebElement el) {
        try {
            String t = el.getText();
            if (t != null && !t.isBlank()) return t;
            return el.getAttribute("text");
        } catch (Exception e) {
            return null;
        }
    }

    private void maybeHideKeyboard() {
        try {
            if (driver instanceof HasOnScreenKeyboard hasKb) {
                if (!hasKb.isKeyboardShown()) return;
            }
        } catch (Exception ignored) {
            return;
        }

        try {
            if (driver instanceof AndroidDriver ad) {
                ad.pressKey(new KeyEvent(AndroidKey.BACK));
            } else {
                driver.executeScript("mobile: pressKey", Map.of("keycode", 4));
            }
        } catch (Exception ignored) { }
    }

}
