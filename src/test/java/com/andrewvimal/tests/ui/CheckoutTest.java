package com.andrewvimal.tests.ui;

import com.andrewvimal.framework.pages.CheckoutPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseUiTest {

    @Test(groups = {"ui", "smoke", "e2e"}, description = "User can buy two products end to end")
    public void completePurchaseEndToEnd() {
        CheckoutPage checkout = loginAsStandardUser()
                .addToCart("Sauce Labs Backpack")
                .addToCart("Sauce Labs Bike Light")
                .openCart()
                .checkout()
                .enterDetails("Andrew", "Tester", "600089");

        // Backpack $29.99 + Bike Light $9.99
        Assert.assertEquals(checkout.itemTotal(), 39.98, 0.001, "Item total before tax");

        checkout.finish();
        Assert.assertEquals(checkout.confirmationText(), "Thank you for your order!");
    }

    @Test(groups = {"ui", "regression"}, description = "Checkout blocks the user when first name is missing")
    public void checkoutRequiresFirstName() {
        CheckoutPage checkout = loginAsStandardUser()
                .addToCart("Sauce Labs Backpack")
                .openCart()
                .checkout()
                .enterDetails("", "Tester", "600089");

        Assert.assertTrue(checkout.errorText().contains("First Name is required"));
    }
}
