package com.andrewvimal.tests.ui;

import com.andrewvimal.framework.config.ConfigReader;
import com.andrewvimal.framework.driver.DriverFactory;
import com.andrewvimal.framework.pages.InventoryPage;
import com.andrewvimal.framework.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/** Every UI test gets a fresh browser, so tests never depend on each other. */
public abstract class BaseUiTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }

    protected InventoryPage loginAsStandardUser() {
        return new LoginPage(driver()).open()
                .loginAs(ConfigReader.get("standardUser"), ConfigReader.get("password"));
    }
}
