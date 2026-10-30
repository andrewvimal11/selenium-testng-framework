package com.andrewvimal.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class InventoryPage extends BasePage {

    private final By title = By.cssSelector("[data-test='title']");
    private final By itemNames = By.cssSelector("[data-test='inventory-item-name']");
    private final By itemPrices = By.cssSelector("[data-test='inventory-item-price']");
    private final By sortDropdown = By.cssSelector("[data-test='product-sort-container']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public String pageTitle() {
        return text(title);
    }

    public List<String> productNames() {
        visible(itemNames);
        return driver.findElements(itemNames).stream().map(WebElement::getText).toList();
    }

    public List<Double> productPrices() {
        visible(itemPrices);
        return driver.findElements(itemPrices).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }

    /** Values on saucedemo: az, za, lohi, hilo */
    public InventoryPage sortBy(String value) {
        new Select(visible(sortDropdown)).selectByValue(value);
        return this;
    }

    /** Builds the add-to-cart button id from the product name, e.g. "Sauce Labs Backpack". */
    public InventoryPage addToCart(String productName) {
        String id = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        click(By.id(id));
        return this;
    }

    public int cartCount() {
        return isDisplayed(cartBadge) ? Integer.parseInt(text(cartBadge)) : 0;
    }

    public CartPage openCart() {
        clickAndWaitForUrl(cartLink, "cart.html");
        return new CartPage(driver);
    }
}
