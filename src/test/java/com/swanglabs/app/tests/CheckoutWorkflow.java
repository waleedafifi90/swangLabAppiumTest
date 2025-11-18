package com.swanglabs.app.tests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import org.testng.Assert;
import org.testng.annotations.*;

import com.swanglabs.app.pages.Cartpage;
import com.swanglabs.app.pages.Checkoutpage;
import com.swanglabs.app.pages.Completepage;
import com.swanglabs.app.pages.Loginpage;
import com.swanglabs.app.pages.Overviewpage;
import com.swanglabs.app.pages.Productpage;

import java.net.URL;

public class CheckoutWorkflow {
	private AndroidDriver driver;

	@BeforeClass
	public void setUp() throws Exception {
		UiAutomator2Options options = new UiAutomator2Options().setPlatformName("Android")
				.setDeviceName("emulator-5554").setAutomationName("UiAutomator2").setAppPackage("com.swaglabsmobileapp")
				.setAppActivity("com.swaglabsmobileapp.MainActivity").setNoReset(false);

		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), options);
	}

	@Test(priority = 1)
	public void testInvalidLogin() {

		Loginpage login = new Loginpage(driver);
		login.login("wrong_user", "wrong_pass");

		String error = login.getErrorMessage();

		Assert.assertTrue(error.contains("Username and password do not match"),
				"Invalid login error message is incorrect!");
	}

	@Test(priority = 2)
	public void testValidLogin() {

		Loginpage login = new Loginpage(driver);
		login.login("standard_user", "secret_sauce");

		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}
	}

	@Test(priority = 3)
	public void testAddOneItemToCart() {

		Productpage product = new Productpage(driver);
		product.addItemToCart();
		product.openCart();

		Cartpage cart = new Cartpage(driver);
		Assert.assertTrue(cart.isItemDisplayed(), "Item was not added to cart");
		cart.goBackToProducts();

		product.removeItemFromCart();

	}

	@Test(priority = 4)
	public void addMultipleItemsToCart() throws InterruptedException {
		Productpage product = new Productpage(driver);
		product.addItemToCart();
		product.addItemToCart();
		product.scrollDown();
		product.addItemToCart();
		product.verfieAddMultiple();

	}

	@Test(priority = 5)
	public void removeItemFromCart() {
		Productpage product = new Productpage(driver);
		product.openCart();
		Cartpage cart = new Cartpage(driver);
		cart.removeFromCart();
		cart.removeFromCart();
	}

	@Test(priority = 6)
	public void checkoutInfoEmpty() throws InterruptedException {

		Cartpage cart = new Cartpage(driver);
		cart.goToCheckout();

		Checkoutpage checkout = new Checkoutpage(driver);
		checkout.continueToNext();

		String error = checkout.getErrorMessage();

		Assert.assertTrue(error.contains("First Name is required"), "Checkout error message is incorrect!");
	}

	@Test(priority = 7)
	public void checkoutMissLastname() throws InterruptedException {
		Checkoutpage checkout = new Checkoutpage(driver);
		checkout.fillCheckoutInformation("Sami", "", "12345");
		checkout.continueToNext();

		String error = checkout.getErrorMessage();

		Assert.assertTrue(error.contains("Last Name is required"), "there is no error");
	}

	@Test(priority = 8)
	public void checkoutMissZipcode() throws InterruptedException {
		Checkoutpage checkout = new Checkoutpage(driver);
		checkout.fillCheckoutInformation("Sami", "Daraghmeh", "");
		checkout.continueToNext();

		String error = checkout.getErrorMessage();

		Assert.assertTrue(error.contains("Postal Code is required"), "there is no error");
	}

	@Test(priority = 9)
	public void checkoutSuccess() throws InterruptedException {
		Checkoutpage checkout = new Checkoutpage(driver);
		checkout.fillCheckoutInformation("Sami", "Daraghmeh", "12345");
		checkout.continueToNext();
	}

	@Test(priority = 10)
	public void getOrder() throws InterruptedException {
		Overviewpage overview = new Overviewpage(driver);
		overview.Overview();
	}

	@Test(priority = 11)
	public void finishOrder() {
		Overviewpage overview = new Overviewpage(driver);
		overview.scrollDown();
		overview.finishOrder();
	}

	@Test(priority = 12)
	public void checkoutComplete() throws InterruptedException {
		Completepage confirmation = new Completepage(driver);
		confirmation.isFinishMessageDisplayed();
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
