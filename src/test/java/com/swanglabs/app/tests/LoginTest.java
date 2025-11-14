package com.swanglabs.app.tests;

import io.appium.java_client.AppiumBy;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

	private static final String USERNAME = "test-Username";
	private static final String PASSWORD = "test-Password";
	private static final String LOGIN = "test-LOGIN";
	private static final String STANDARD_USER_TEXT = "standard_user";
	private static final String TESTMENU = "test-Menu";
	private static final String DRAWERCLOSEBUTTON = "test-Close";
	private static final String LOCKED_OUT_USER = "locked_out_user";
	private static final String LOGOUT = "LOGOUT";
	private static final String PROBLEM_USER = "problem_user";
	
	private final By usernameField = AppiumBy.accessibilityId(USERNAME);
	private final By passwordField = AppiumBy.accessibilityId(PASSWORD);
	private final By loginButton = AppiumBy.accessibilityId(LOGIN);
	private final By hamburger = AppiumBy.accessibilityId(TESTMENU);
	private final By drawerClose = AppiumBy.accessibilityId(DRAWERCLOSEBUTTON);
	private final By logoutButton = AppiumBy.androidUIAutomator("new UiSelector().text(\""+LOGOUT+"\")");
	

	@Test(priority = 0)
	public void validLoginWorks() throws InterruptedException {
		wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));

		actions.scrollIntoViewByText(STANDARD_USER_TEXT);
		WebElement el = driver
				.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"" + STANDARD_USER_TEXT + "\"]"));
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
		wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(cart),
				ExpectedConditions.visibilityOfElementLocated(productsTitle)));
	}

	@Test(priority = 1)
	public void drawerOpensAndHasExpectedItems() {
		By hamburger = AppiumBy.accessibilityId("test-Menu");
		actions.openDrawer(hamburger, "ALL ITEMS");
		actions.assertDrawerItemsPresent("ALL ITEMS", "ABOUT", "LOGOUT", "RESET APP STATE");
		Assert.assertTrue(actions.isDrawerOpen(drawerClose, "ALL ITEMS"), "Drawer did not open");

		actions.clickDrawerItemAndWaitClose("ALL ITEMS", drawerClose);
		Assert.assertFalse(actions.isDrawerOpen(drawerClose, "ALL ITEMS"),
				"Drawer should be closed after clicking item");
	}
	
	@Test(priority = 2)
	public void drawer_close_button_closes_drawer() {
	    actions.openDrawer(hamburger, "ALL ITEMS");
	    Assert.assertTrue(
	        actions.isDrawerOpen(drawerClose, "ALL ITEMS"),
	        "Drawer did not open"
	    );

	    actions.closeDrawer(drawerClose);

	    Assert.assertFalse(
	        actions.isDrawerOpen(drawerClose, "ALL ITEMS"),
	        "Drawer should be closed after tapping the close button"
	    );
	}
	
	@Test(priority = 3)
	public void product_title_and_price_match_between_list_and_details() {

	    By listItemTitle = AppiumBy.xpath("//android.widget.TextView[@content-desc=\"test-Item title\"]");
	    By listItemPrice = AppiumBy.xpath("//android.widget.TextView[@content-desc=\"test-Price\"]");
	    
	    List<WebElement> itemsTitle = wait.until(
	            ExpectedConditions.visibilityOfAllElementsLocatedBy(listItemTitle));
	    List<WebElement> itemsPrice = wait.until(
	    		ExpectedConditions.visibilityOfAllElementsLocatedBy(listItemPrice));
	    
	    WebElement firstItem = itemsTitle.get(0);
	    String chosenTitle = actions.safeGetTextOrAttr(itemsTitle.get(0));
	    String chosenPrice = actions.safeGetTextOrAttr(itemsPrice.get(0));
	    
	    
	    actions.clickReliable(firstItem);
	    
	    By detailsDescriptionView = AppiumBy.xpath("//android.widget.ScrollView[@content-desc=\"test-Inventory item page\"]//android.view.ViewGroup[@content-desc=\"test-Description\"]//android.widget.TextView"); 
	    List<WebElement> viewList = wait.until(
	            ExpectedConditions.visibilityOfAllElementsLocatedBy(detailsDescriptionView));
	    
	    WebElement detailsPagePrice = actions.scrollDownTo(AppiumBy.accessibilityId("test-Price"));

	    
	    String detailsTitle = actions.safeGetTextOrAttr(viewList.get(0));
	    String detailsPrice = actions.safeGetTextOrAttr(detailsPagePrice);
    
	    Assert.assertEquals(chosenTitle, detailsTitle,
	            String.format("Expected exact text '%s' but was '%s'", chosenTitle, detailsTitle));
	    
	    Assert.assertEquals(detailsPrice, chosenPrice,
	    		String.format("Expected exact text '%s' but was '%s'", detailsPrice, chosenPrice));
	    	    
	}
	
	@Test(priority = 5)
	public void testLogOut() {
		By hamburger = AppiumBy.accessibilityId("test-Menu");
		actions.openDrawer(hamburger, LOGOUT);
		actions.clickDrawerItemAndWaitClose(LOGOUT, logoutButton);

	}
	
	@Test(priority = 6)
	public void lockedOutUserShowsError() throws InterruptedException {
		wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));

		actions.scrollIntoViewByText(LOCKED_OUT_USER);
		WebElement el = driver
				.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"" + LOCKED_OUT_USER + "\"]"));
		Thread.sleep(2000);
		actions.tryClick(el);

		actions.scrollIntoViewByDesc(LOGIN);

		WebElement loginBtn = driver.findElement(loginButton);
		actions.tryClick(loginBtn);

	    By errorMsg = AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-Error message\"]//android.widget.TextView");
	    
	    String msg = actions.safeGetTextOrAttr(errorMsg);
	    Assert.assertTrue(msg != null && msg.toLowerCase().contains("locked"),
	            "Expected error to mention 'locked', but was: " + msg);

	}
	
	
	@Test(priority = 7)
	public void problemUserTest() throws InterruptedException {
		wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));
		
		actions.scrollIntoViewByText(PROBLEM_USER);
		WebElement el = driver
				.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"" + PROBLEM_USER + "\"]"));
		Thread.sleep(2000);
		actions.tryClick(el);

		actions.scrollIntoViewByDesc(LOGIN);

		WebElement loginBtn = driver.findElement(loginButton);
		actions.tryClick(loginBtn);
		
		 By listItemTitle = AppiumBy.xpath("//android.widget.TextView[@content-desc=\"test-Item title\"]");
		    By listItemPrice = AppiumBy.xpath("//android.widget.TextView[@content-desc=\"test-Price\"]");
		    
		    List<WebElement> itemsTitle = wait.until(
		            ExpectedConditions.visibilityOfAllElementsLocatedBy(listItemTitle));
		    List<WebElement> itemsPrice = wait.until(
		    		ExpectedConditions.visibilityOfAllElementsLocatedBy(listItemPrice));
		    
		    WebElement firstItem = itemsTitle.get(0);
		    String chosenTitle = actions.safeGetTextOrAttr(itemsTitle.get(0));
		    String chosenPrice = actions.safeGetTextOrAttr(itemsPrice.get(0));
		    
		    
		    actions.clickReliable(firstItem);
		    
		    By detailsDescriptionView = AppiumBy.xpath("//android.widget.ScrollView[@content-desc=\"test-Inventory item page\"]//android.view.ViewGroup[@content-desc=\"test-Description\"]//android.widget.TextView"); 
		    List<WebElement> viewList = wait.until(
		            ExpectedConditions.visibilityOfAllElementsLocatedBy(detailsDescriptionView));
		    
		    WebElement detailsPagePrice = actions.scrollDownTo(AppiumBy.accessibilityId("test-Price"));

		    
		    String detailsTitle = actions.safeGetTextOrAttr(viewList.get(0));
		    String detailsPrice = actions.safeGetTextOrAttr(detailsPagePrice);
	    
		    Assert.assertEquals(chosenTitle, detailsTitle,
		            String.format("Expected exact text '%s' but was '%s'", chosenTitle, detailsTitle));
		    
		    Assert.assertEquals(detailsPrice, chosenPrice,
		    		String.format("Expected exact text '%s' but was '%s'", detailsPrice, chosenPrice));

	}
	

}
