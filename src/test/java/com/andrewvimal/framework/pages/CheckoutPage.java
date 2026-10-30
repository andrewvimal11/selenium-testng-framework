package com.andrewvimal.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Covers the three checkout steps: your information, overview, and completion. */
public class CheckoutPage extends BasePage {

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By itemTotal = By.cssSelector("[data-test='subtotal-label']");
    private final By finishButton = By.id("finish");
    private final By completeHeader = By.cssSelector("[data-test='complete-header']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage enterDetails(String first, String last, String zip) {
        type(firstName, first);
        type(lastName, last);
        type(postalCode, zip);
        click(continueButton);
        return this;
    }

    public String errorText() {
        return text(errorMessage);
    }

    /** "Item total: $29.99" -> 29.99 */
    public double itemTotal() {
        return Double.parseDouble(text(itemTotal).replaceAll("[^0-9.]", ""));
    }

    public CheckoutPage finish() {
        clickAndWaitForUrl(finishButton, "checkout-complete");
        return this;
    }

    public String confirmationText() {
        return text(completeHeader);
    }
}
