package com.andrewvimal.tests.api;

import com.andrewvimal.framework.api.BookingClient;
import com.andrewvimal.framework.api.models.Booking;
import com.andrewvimal.framework.api.models.BookingDates;
import com.andrewvimal.framework.config.ConfigReader;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Full CRUD lifecycle against Restful Booker. Tests are chained with dependsOnMethods
 * because each step works on the booking created in the first one.
 */
public class BookingApiTest {

    private BookingClient client;
    private String token;
    private int bookingId;
    private Booking original;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        client = new BookingClient();
        token = client.createToken(ConfigReader.get("apiUser"), ConfigReader.get("apiPassword"));
        original = new Booking("Andrew", "Tester", 250, true,
                new BookingDates("2026-12-01", "2026-12-05"), "Breakfast");
    }

    @Test(groups = {"api", "smoke"}, description = "Auth endpoint returns a token for valid credentials")
    public void authReturnsToken() {
        Assert.assertNotNull(token);
        Assert.assertFalse(token.isBlank());
    }

    @Test(groups = {"api", "smoke"}, description = "POST /booking creates a booking and echoes the data back")
    public void createBooking() {
        Response response = client.createBooking(original);

        Assert.assertEquals(response.statusCode(), 200);
        bookingId = response.path("bookingid");
        Assert.assertTrue(bookingId > 0, "bookingid should be positive");

        Booking created = response.jsonPath().getObject("booking", Booking.class);
        Assert.assertEquals(created.getFirstname(), original.getFirstname());
        Assert.assertEquals(created.getTotalprice(), original.getTotalprice());
        Assert.assertEquals(created.getBookingdates().getCheckin(), "2026-12-01");
    }

    @Test(dependsOnMethods = "createBooking", groups = {"api", "regression"},
            description = "GET /booking/{id} returns the booking that was created")
    public void getBooking() {
        Response response = client.getBooking(bookingId);

        Assert.assertEquals(response.statusCode(), 200);
        Booking fetched = response.as(Booking.class);
        Assert.assertEquals(fetched.getLastname(), original.getLastname());
        Assert.assertEquals(fetched.getAdditionalneeds(), original.getAdditionalneeds());
    }

    @Test(dependsOnMethods = "getBooking", groups = {"api", "regression"},
            description = "PUT /booking/{id} with a token updates the booking")
    public void updateBooking() {
        Booking updated = new Booking("Andrew", "Tester", 400, false,
                new BookingDates("2026-12-10", "2026-12-12"), "Late checkout");

        Response response = client.updateBooking(bookingId, updated, token);

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.as(Booking.class).getTotalprice(), 400);
    }

    @Test(groups = {"api", "regression"}, description = "PUT without a token is rejected")
    public void updateWithoutTokenIsForbidden() {
        Response response = client.updateBooking(1, original, "invalid-token");

        Assert.assertEquals(response.statusCode(), 403);
    }

    @Test(dependsOnMethods = "updateBooking", groups = {"api", "regression"},
            description = "DELETE /booking/{id} removes the booking")
    public void deleteBooking() {
        Assert.assertEquals(client.deleteBooking(bookingId, token).statusCode(), 201);
        Assert.assertEquals(client.getBooking(bookingId).statusCode(), 404);
    }
}
