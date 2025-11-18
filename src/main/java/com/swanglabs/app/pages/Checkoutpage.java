package com.swanglabs.app.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;

public class Checkoutpage {
    private AndroidDriver driver;
    private WebDriverWait wait;

    private By continueButton = By.xpath("//android.view.ViewGroup[@content-desc=\"test-CONTINUE\"]");
    private By errormessage = By.xpath("//android.view.ViewGroup[@content-desc=\"test-Error message\"]");
    private By firstNameField = By.xpath("//android.widget.EditText[@content-desc=\"test-First Name\"]");
    private By lastNameField = By.xpath("//android.widget.EditText[@content-desc=\"test-Last Name\"]");
    private By zipCodeField = By.xpath("//android.widget.EditText[@content-desc=\"test-Zip/Postal Code\"]");

    public Checkoutpage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
    }

    public void continueToNext() {
    	if(driver.isKeyboardShown()){   driver.hideKeyboard();}
        WebElement continueBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(continueButton));
        continueBtn.click();
    }

    public String getErrorMessage() {
    WebElement error = wait.until(
        ExpectedConditions.visibilityOfElementLocated(errormessage)
    );
    return error.findElement(By.xpath(".//android.widget.TextView")).getText().trim();
}

public void fillCheckoutInformation(String firstName, String lastName, String zipCode) {
    WebElement firstNameElem = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
    firstNameElem.click();
    firstNameElem.clear();
    firstNameElem.sendKeys(firstName);

    WebElement lastNameElem = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField));
    lastNameElem.click();
    lastNameElem.clear();
    lastNameElem.sendKeys(lastName);

    WebElement zipCodeElem = wait.until(ExpectedConditions.visibilityOfElementLocated(zipCodeField));
    zipCodeElem.click();
    zipCodeElem.clear();
    zipCodeElem.sendKeys(zipCode);
}

}
