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
	    try {
	        String t = el.getText();
	        if (t != null && !t.isBlank()) {
	            return t.trim();
	        }

	        t = el.getAttribute("text");
	        if (t != null && !t.isBlank()) {
	            return t.trim();
	        }

	        t = el.getAttribute("content-desc");
	        if (t != null && !t.isBlank()) {
	            return t.trim();
	        }

	        t = el.getAttribute("contentDescription");
	        if (t != null && !t.isBlank()) {
	            return t.trim();
	        }

	        return "";
	    } catch (Exception e) {
	        return "";
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
}
