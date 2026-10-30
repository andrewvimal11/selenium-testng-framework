package com.andrewvimal.tests.ui;

import com.andrewvimal.framework.config.ConfigReader;
import com.andrewvimal.framework.pages.InventoryPage;
import com.andrewvimal.framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseUiTest {

    @Test(groups = {"ui", "smoke"}, description = "Valid user lands on the Products page")
    public void validLoginShowsProducts() {
        InventoryPage inventory = loginAsStandardUser();

        Assert.assertEquals(inventory.pageTitle(), "Products");
        Assert.assertTrue(inventory.currentUrl().contains("/inventory.html"));
    }

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        String pw = ConfigReader.get("password");
        return new Object[][]{
                {"", pw, "Username is required"},
                {ConfigReader.get("standardUser"), "", "Password is required"},
                {ConfigReader.get("standardUser"), "wrong_password", "Username and password do not match"},
                {ConfigReader.get("lockedUser"), pw, "Sorry, this user has been locked out"},
        };
    }

    @Test(dataProvider = "invalidLogins", groups = {"ui", "regression"},
            description = "Invalid credentials show the correct error message")
    public void invalidLoginShowsError(String user, String pass, String expectedError) {
        LoginPage login = new LoginPage(driver()).open().submitExpectingError(user, pass);

        Assert.assertTrue(login.errorText().contains(expectedError),
                "Expected error to contain '" + expectedError + "' but was: " + login.errorText());
    }
}
