package com.andrewvimal.framework.pages;

import com.andrewvimal.framework.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Shared actions for every page object. All interactions go through explicit waits,
 * which is the main defence against flaky tests (no Thread.sleep anywhere).
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicitWaitSeconds")));
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /**
     * Clicks something that should navigate, then waits until the URL changes.
     * If navigation doesn't happen, the failure names the page we expected instead of
     * failing later on a missing element.
     */
    protected void clickAndWaitForUrl(By locator, String expectedUrlPart) {
        click(locator);
        new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicitWaitSeconds")))
                .withMessage("Expected navigation to a URL containing '" + expectedUrlPart + "'")
                .until(ExpectedConditions.urlContains(expectedUrlPart));
    }

    protected void type(By locator, String text) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
