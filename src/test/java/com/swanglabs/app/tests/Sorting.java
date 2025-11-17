package com.swanglabs.app.tests;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;

public class Sorting extends BaseTest {
    private static final String USERNAME = "test-Username";
    private static final String PASSWORD = "test-Password";
    private static final String LOGIN = "test-LOGIN";
    private static final String STANDARD_USER_TEXT = "standard_user";
    private final By usernameField = AppiumBy.accessibilityId(USERNAME);
    private final By passwordField = AppiumBy.accessibilityId(PASSWORD);
    private final By loginButton   = AppiumBy.accessibilityId(LOGIN);
    @Test
    public void testProductSortingAndDetails() throws InterruptedException {
        
    	                                     // ------------------ LOGIN ------------------
        
    	wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));
        actions.scrollIntoViewByText(STANDARD_USER_TEXT);
        WebElement userChoice = driver.findElement(By.xpath("//android.widget.TextView[@text='" + STANDARD_USER_TEXT + "']"));
        actions.clickReliable(userChoice);
        Thread.sleep(1000);
        actions.recoverIfBackgrounded();
        WebElement userEl = actions.scrollIntoViewByDesc(USERNAME);
        actions.clickReliable(userEl);
        actions.ensureFilled(usernameField, "standard_user");
        actions.ensureFilled(passwordField, "secret_sauce");
        actions.assertHasText(usernameField, "Username should contain text");
        actions.assertHasText(passwordField, "Password should contain text");
        actions.clickReliable(driver.findElement(loginButton));
        
                                              // ------------------ SORTING ------------------
        
        WebElement sortBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@content-desc='test-Modal Selector Button']")));
        actions.clickReliable(sortBtn);
        Thread.sleep(800);
        WebElement sortAZ = wait.until(ExpectedConditions.visibilityOfElementLocated( By.xpath("//*[@text='Name (A to Z)']")));
        actions.clickReliable(sortAZ);
        Thread.sleep(800);
        WebElement sortBtn1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@content-desc='test-Modal Selector Button']")));
        actions.clickReliable(sortBtn1);
        Thread.sleep(800);
        WebElement sortZA = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Name (Z to A)']") ));
        actions.clickReliable(sortZA);
        Thread.sleep(800);
        WebElement sortBtn2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@content-desc='test-Modal Selector Button']")));
        actions.clickReliable(sortBtn2);
        Thread.sleep(800);
        WebElement sortLowHigh = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Price (low to high)']")));
        actions.clickReliable(sortLowHigh);
        Thread.sleep(800);
        WebElement sortBtn3 = wait.until( ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@content-desc='test-Modal Selector Button']")));
        actions.clickReliable(sortBtn3);
        Thread.sleep(800);
        WebElement sortHighLow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Price (high to low)']")));
        actions.clickReliable(sortHighLow);
        Thread.sleep(800);
        
                                              // ------------------ READ FIRST PRODUCT ------------------
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@content-desc='test-Item title']")));
        List<WebElement> titles = driver.findElements( By.xpath("//*[@content-desc='test-Item title']"));
        List<WebElement> prices = driver.findElements(By.xpath("//*[@content-desc='test-Price']") );
        Assert.assertFalse(titles.isEmpty(), "No products found on PRODUCTS screen!");
        Assert.assertEquals(titles.size(), prices.size(),"Titles count and prices count do not match!");
        WebElement firstTitleEl = titles.get(0);
        WebElement firstPriceEl = prices.get(0);
        String expectedName  = actions.safeGetTextOrAttr(firstTitleEl);
        String expectedPrice = actions.safeGetTextOrAttr(firstPriceEl);
        Assert.assertNotNull(expectedName,  "Could not read product name text!");
        Assert.assertFalse(expectedName.isBlank(), "Product name text is blank!");
        Assert.assertNotNull(expectedPrice, "Could not read product price text!");
        Assert.assertFalse(expectedPrice.isBlank(), "Product price text is blank!");
        System.out.println("LIST  name : " + expectedName);
        System.out.println("LIST  price: " + expectedPrice);
        actions.clickReliable(firstTitleEl);
        
                                              // ------------------ READ THE PRODUCT FROM THE INSIDE ------------------
        
        WebElement detailNameEl = actions.scrollIntoViewByText(expectedName); 
        String actualName = actions.safeGetTextOrAttr(detailNameEl);
        WebElement detailPriceEl = driver.findElement(By.xpath("//android.widget.TextView[@content-desc='test-Price']"));
        String actualPrice = detailPriceEl.getText().trim();
        System.out.println("DETAIL name : " + actualName);
        System.out.println("DETAIL price: " + actualPrice); 
        Assert.assertEquals( actualName, expectedName,"Product name in details screen does NOT match list screen!");
        Assert.assertEquals(actualPrice, expectedPrice,"Product price in details screen does NOT match list screen!");
        
                                              // ------------------ BACK TO PRODUCTS ------------------
        
        WebElement backToProductsBtn = actions.scrollIntoViewByDesc("test-BACK TO PRODUCTS");
        actions.clickReliable(backToProductsBtn);
        driver.quit();
  
    }
}
