package com.andrewvimal.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private final By itemNames = By.cssSelector("[data-test='inventory-item-name']");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> itemNames() {
        visible(checkoutButton);
        return driver.findElements(itemNames).stream().map(WebElement::getText).toList();
    }

    public CheckoutPage checkout() {
        clickAndWaitForUrl(checkoutButton, "checkout-step-one");
        return new CheckoutPage(driver);
    }
}
