package com.andrewvimal.tests.ui;

import com.andrewvimal.framework.pages.CartPage;
import com.andrewvimal.framework.pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InventoryTest extends BaseUiTest {

    @Test(groups = {"ui", "regression"}, description = "Sorting by price low-to-high orders products correctly")
    public void sortByPriceLowToHigh() {
        InventoryPage inventory = loginAsStandardUser().sortBy("lohi");

        List<Double> actual = inventory.productPrices();
        List<Double> expected = new ArrayList<>(actual);
        expected.sort(Comparator.naturalOrder());
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = {"ui", "regression"}, description = "Sorting by name Z-A orders products correctly")
    public void sortByNameZtoA() {
        InventoryPage inventory = loginAsStandardUser().sortBy("za");

        List<String> actual = inventory.productNames();
        List<String> expected = new ArrayList<>(actual);
        expected.sort(Comparator.reverseOrder());
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = {"ui", "smoke"}, description = "Adding products updates the cart badge and cart contents")
    public void addProductsToCart() {
        InventoryPage inventory = loginAsStandardUser()
                .addToCart("Sauce Labs Backpack")
                .addToCart("Sauce Labs Bike Light");

        Assert.assertEquals(inventory.cartCount(), 2);

        CartPage cart = inventory.openCart();
        Assert.assertEquals(cart.itemNames(), List.of("Sauce Labs Backpack", "Sauce Labs Bike Light"));
    }
}
