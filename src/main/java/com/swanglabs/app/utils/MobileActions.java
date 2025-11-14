package com.swanglabs.app.utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HasOnScreenKeyboard;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class MobileActions {

	private final AppiumDriver driver;
	private final WebDriverWait wait;
	private final String appPackage;

	public MobileActions(AppiumDriver driver, WebDriverWait wait) {
		this(driver, wait, null);
	}

	public MobileActions(AppiumDriver driver, WebDriverWait wait, String appPackage) {
		this.driver = driver;
		this.wait = wait;
		this.appPackage = appPackage;
	}

	public WebElement scrollIntoViewByText(String text) {
		String ui = "new UiScrollable(new UiSelector().scrollable(true))" + ".setAsVerticalList()"
				+ ".scrollIntoView(new UiSelector().text(\"" + text + "\"));";
		return driver.findElement(AppiumBy.androidUIAutomator(ui));
	}

	public WebElement scrollIntoViewByDesc(String desc) {
		String ui = "new UiScrollable(new UiSelector().scrollable(true))" + ".setAsVerticalList()"
				+ ".scrollIntoView(new UiSelector().description(\"" + desc + "\"));";
		return driver.findElement(AppiumBy.androidUIAutomator(ui));
	}

	public void centerElementInViewSafe(WebElement el) {
		Rectangle r = el.getRect();
		Dimension win = driver.manage().window().getSize();

		int topSafe = (int) (win.getHeight() * 0.10);
		int bottomSafe = (int) (win.getHeight() * 0.90);

		boolean tooTop = r.getY() < topSafe;
		boolean tooBottom = (r.getY() + r.getHeight()) > bottomSafe;

		int midX = win.getWidth() / 2;
		if (tooTop) {
			smallSwipeSafe(midX, (int) (win.getHeight() * 0.35), midX, (int) (win.getHeight() * 0.65), 250);
		} else if (tooBottom) {
			smallSwipeSafe(midX, (int) (win.getHeight() * 0.65), midX, (int) (win.getHeight() * 0.35), 250);
		}
	}

	public void smallSwipeSafe(int sx, int sy, int ex, int ey, int durationMs) {
		Dimension win = driver.manage().window().getSize();
		int minY = (int) (win.getHeight() * 0.20);
		int maxY = (int) (win.getHeight() * 0.80);

		sy = Math.max(minY, Math.min(sy, maxY));
		ey = Math.max(minY, Math.min(ey, maxY));

		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence swipe = new Sequence(finger, 1);
		swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), sx, sy));
		swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
		swipe.addAction(
				finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), ex, ey));
		swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
		driver.perform(List.of(swipe));
	}

	public boolean tryClick(WebElement element) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element));
			element.click();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public void tapCenter(WebElement el) {
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

	public void ensureFilled(By fieldLocator, String desiredValue) {
		WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(fieldLocator));
		String current = safeGetTextOrAttr(field);
		if (current == null || current.isBlank()) {
			field.clear();
			field.sendKeys(desiredValue);
		}
	}

	public void assertHasText(By fieldLocator, String messageIfEmpty) {
		WebElement field = driver.findElement(fieldLocator);
		String current = safeGetTextOrAttr(field);
		Assert.assertTrue(current != null && !current.isBlank(), messageIfEmpty);
	}

	public String safeGetTextOrAttr(WebElement el) {
	    if (el == null) return null;
	    try {
	        String t = el.getText();
	        System.out.println("Printing from here: " + t);
	        if (t != null && !t.isBlank()) return t;
	        String[] attrs = {"text", "content-desc", "name", "label", "value"};
	        for (String a : attrs) {
	            try {
	                String v = el.getAttribute(a);
	                if (v != null && !v.isBlank()) return v.trim();
	            } catch (Exception ignored) {}
	        }
	        return null;
	    } catch (Exception e) {
	        return null;
	    }
	}

	public String safeGetTextOrAttr(By locator) {
	    try {
	        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	        return safeGetTextOrAttr(el);
	    } catch (Exception e) {
	        return null;
	    }
	}
	

	public void maybeHideKeyboard() {
		try {
			if (driver instanceof HasOnScreenKeyboard hasKb) {
				if (!hasKb.isKeyboardShown())
					return;
			} else {
				return;
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
		} catch (Exception ignored) {
		}
	}

	public void recoverIfBackgrounded() {
		if (!(driver instanceof AndroidDriver ad))
			return;
		if (appPackage == null || appPackage.isBlank())
			return;

		try {
			String currentPkg = ad.getCurrentPackage();
			if (currentPkg == null || !currentPkg.equals(appPackage)) {
				ad.activateApp(appPackage);
				Thread.sleep(300);
			}
		} catch (Exception ignored) {
		}
	}

	public void clickReliable(WebElement el) {
		if (!tryClick(el)) {
			tapCenter(el);
		}
	}
	
	public void openDrawer(By hamburgerButton, String... expectedVisibleItems) {
	    WebElement drawerBtn = driver.findElement(hamburgerButton);
	    clickReliable(drawerBtn);

	    if (expectedVisibleItems != null && expectedVisibleItems.length > 0) {
	        waitForMenuItemVisible(expectedVisibleItems[0], 10);
	    }
	}

	public void assertDrawerItemsPresent(String... labels) {
	    for (String label : labels) {
	        WebElement item = waitForMenuItemVisible(label, 10);
	        Assert.assertNotNull(item, "Menu item is not visible: " + label);
	    }
	}

	public WebElement waitForMenuItemVisible(String label, long timeoutSec) {
	    By byItem = AppiumBy.accessibilityId("test-" + label);
	    try {
	        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
	                .until(ExpectedConditions.visibilityOfElementLocated(byItem));
	    } catch (Exception ignored) {}

	    try {
	        By byText = AppiumBy.androidUIAutomator("new UiSelector().text(\"" + label + "\")");
	        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
	                .until(ExpectedConditions.visibilityOfElementLocated(byText));
	    } catch (Exception ignored) {}

	    try {
	        By byContainsText = By.xpath("//*[contains(@text, \"" + label + "\")]");
	        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
	                .until(ExpectedConditions.visibilityOfElementLocated(byContainsText));
	    } catch (Exception e) {
	        throw new AssertionError("Could not find menu item with label: " + label, e);
	    }
	}
	
	public void clickDrawerItemAndWaitClose(String label, By closeButton) {
	    WebElement item = waitForMenuItemVisible(label, 10);
	    clickReliable(item);
	    waitDrawerClosed(closeButton, 10);
	}

	public void closeDrawer(By closeButton) {
	    clickReliable(driver.findElement(closeButton));
	    waitDrawerClosed(closeButton, 10);
	}

	public void waitDrawerClosed(By closeButton, long timeoutSec) {
	    new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
	        .until(ExpectedConditions.invisibilityOfElementLocated(closeButton));
	}
	
	public boolean isDrawerOpen(By closeButton, String probeItemLabel) {
	    try {
	        new WebDriverWait(driver, Duration.ofSeconds(2))
	            .until(ExpectedConditions.or(
	                ExpectedConditions.visibilityOfElementLocated(closeButton),
	                ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("test-" + probeItemLabel)),
	                ExpectedConditions.visibilityOfElementLocated(
	                    AppiumBy.androidUIAutomator("new UiSelector().text(\"" + probeItemLabel + "\")")
	                )
	            ));
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	
	// ChatGPT for scrolling to the locator
	// ===================== SIMPLE SCROLL DOWN TO LOCATOR =====================

	/** Scrolls down (swipe up) until the element located by `locator` is visible. */
	public WebElement scrollDownTo(By locator, int maxSwipes) {
	    maybeHideKeyboard(); // optional: avoid keyboard covering content
	    for (int i = 0; i < maxSwipes; i++) {
	        WebElement el = waitVisibleQuick(locator, 600);
	        if (el != null) return el;
	        swipeUpOnePage(300);
	    }
	    throw new org.openqa.selenium.NoSuchElementException(
	            "Element not found after scrolling: " + locator);
	}

	/** Convenience overload: defaults to 8 swipes. */
	public WebElement scrollDownTo(By locator) {
	    return scrollDownTo(locator, 8);
	}

	// ---------- tiny private helpers ----------

	private WebElement waitVisibleQuick(By locator, long timeoutMs) {
	    try {
	        return new WebDriverWait(driver, java.time.Duration.ofMillis(timeoutMs))
	                .until(ExpectedConditions.visibilityOfElementLocated(locator));
	    } catch (Exception e) {
	        return null;
	    }
	}

	/** One safe swipe UP (content moves down the screen). */
	private void swipeUpOnePage(int durationMs) {
	    org.openqa.selenium.Dimension win = driver.manage().window().getSize();
	    int x = win.getWidth() / 2;
	    int startY = (int) (win.getHeight() * 0.70);
	    int endY   = (int) (win.getHeight() * 0.30);

	    org.openqa.selenium.interactions.PointerInput finger =
	            new org.openqa.selenium.interactions.PointerInput(
	                    org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
	    org.openqa.selenium.interactions.Sequence swipe = new org.openqa.selenium.interactions.Sequence(finger, 1);
	    swipe.addAction(finger.createPointerMove(java.time.Duration.ZERO,
	            org.openqa.selenium.interactions.PointerInput.Origin.viewport(), x, startY));
	    swipe.addAction(finger.createPointerDown(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
	    swipe.addAction(finger.createPointerMove(java.time.Duration.ofMillis(durationMs),
	            org.openqa.selenium.interactions.PointerInput.Origin.viewport(), x, endY));
	    swipe.addAction(finger.createPointerUp(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
	    driver.perform(java.util.List.of(swipe));
	}


}
