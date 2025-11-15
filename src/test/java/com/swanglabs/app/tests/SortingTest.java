package com.swanglabs.app.tests;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
 
import io.appium.java_client.AppiumBy;
 
public class SortingTest extends BaseTest {
 
    private static final String USERNAME = "test-Username";
    private static final String PASSWORD = "test-Password";
    private static final String LOGIN = "test-LOGIN";
    private static final String STANDARD_USER_TEXT = "standard_user";
    //-------------------------------------------------------------------------------------
    private final By usernameField = AppiumBy.accessibilityId(USERNAME);
    private final By passwordField = AppiumBy.accessibilityId(PASSWORD);
    private final By loginButton   = AppiumBy.accessibilityId(LOGIN);
    //-------------------------------------------------------------------------------------
    @Test
    public void testProductSortingAndDetails() throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));
        actions.scrollIntoViewByText(STANDARD_USER_TEXT);
        WebElement el = driver.findElement(
                By.xpath("//android.widget.TextView[@text='" + STANDARD_USER_TEXT + "']")
        );
        actions.clickReliable(el);
        Thread.sleep(1000);
        //-------------------------------------------------------------------------------------
        actions.recoverIfBackgrounded();
        WebElement userEl = actions.scrollIntoViewByDesc(USERNAME);
        actions.clickReliable(userEl);
        actions.ensureFilled(usernameField, "standard_user");
        actions.ensureFilled(passwordField, "secret_sauce");
        actions.assertHasText(usernameField, "Username should contain text");
        actions.assertHasText(passwordField, "Password should contain text");
        actions.clickReliable(driver.findElement(loginButton));
        //-------------------------------------------------------------------------------------
        WebElement sortBtn = wait.until
        		(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@content-desc=\'test-Modal Selector Button\']"))
                );
        actions.clickReliable(sortBtn);
        Thread.sleep(1200);
        //-------------------------------------------------------------------------------------
        WebElement sortAZ = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Name (A to Z)']"))
        );
        actions.clickReliable(sortAZ);
        Thread.sleep(1200);
        //-------------------------------------------------------------------------------------
        WebElement sortBtn1 = wait.until
        		(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@content-desc=\'test-Modal Selector Button\']"))
                );
        actions.clickReliable(sortBtn1);
        Thread.sleep(1200);
        //-------------------------------------------------------------------------------------
        WebElement sortZA = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Name (Z to A)']"))
        );
        actions.clickReliable(sortZA);
        Thread.sleep(1200);
        //-------------------------------------------------------------------------------------
        WebElement sortBtn2 = wait.until
        		(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@content-desc=\'test-Modal Selector Button\']"))
                );
        actions.clickReliable(sortBtn2);
        Thread.sleep(1200);
        //-------------------------------------------------------------------------------------
        WebElement sortLowHigh = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Price (low to high)']"))
        );
        actions.clickReliable(sortLowHigh);
        Thread.sleep(1200);
        //-------------------------------------------------------------------------------------
        WebElement sortBtn3 = wait.until
        		(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@content-desc=\'test-Modal Selector Button\']"))
                );
        actions.clickReliable(sortBtn3);
        Thread.sleep(1200);
        //-------------------------------------------------------------------------------------
        WebElement sortLowHigh1 = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Price (high to low)']"))
        );
        actions.clickReliable(sortLowHigh1);
        Thread.sleep(1200);
        //-------------------------------------------------------------------------------------
        List<WebElement> productTitles = driver.findElements(By.id("test-Item title"));
        List<String> namesFromApp = new ArrayList<>();
        for (WebElement el1 : productTitles) {
            namesFromApp.add(el1.getText());
        }
        //--------------------------------------------------------------------------------------
        List<String> sortedNames = new ArrayList<>(namesFromApp);
        Collections.sort(sortedNames);
        Assert.assertEquals(namesFromApp, sortedNames, "Sorting A to Z is NOT correct!");
        String expectedName = namesFromApp.get(0);
        actions.clickReliable(productTitles.get(0));
      //---------------------------------------------------------------------------------------
        WebElement productName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("test-Item title"))
        );
        Assert.assertEquals(productName.getText(), expectedName, "Product name does NOT match!");
        Assert.assertTrue(driver.findElement(By.id("test-Item description")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("test-Price")).isDisplayed());
        actions.clickReliable(driver.findElement(By.id("test-BACK TO PRODUCTS")));
    }
}