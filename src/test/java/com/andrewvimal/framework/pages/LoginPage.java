package com.andrewvimal.framework.pages;

import com.andrewvimal.framework.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(ConfigReader.get("baseUrl"));
        visible(loginButton);
        return this;
    }

    public InventoryPage loginAs(String user, String pass) {
        submit(user, pass);
        return new InventoryPage(driver);
    }

    /** Use for negative tests where we expect to stay on the login page. */
    public LoginPage submitExpectingError(String user, String pass) {
        submit(user, pass);
        return this;
    }

    private void submit(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginButton);
    }

    public String errorText() {
        return text(errorMessage);
    }
}
