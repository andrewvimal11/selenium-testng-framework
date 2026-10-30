package com.andrewvimal.framework.api;

import com.andrewvimal.framework.config.ConfigReader;
import com.andrewvimal.framework.api.models.Booking;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Thin client for the Restful Booker API. Tests call these methods
 * instead of building requests inline, so endpoints live in one place.
 */
public class BookingClient {

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri(ConfigReader.get("apiBaseUrl"))
            .setContentType(ContentType.JSON)
            // Must be exactly "application/json": RestAssured's ContentType.JSON sends a list of
            // four media types, and Restful Booker answers that with 418 I'm a Teapot.
            .setAccept("application/json")
            .log(LogDetail.URI)
            .build();

    public String createToken(String username, String password) {
        return given(spec)
                .body(Map.of("username", username, "password", password))
                .post("/auth")
                .then().statusCode(200)
                .extract().path("token");
    }

    public Response createBooking(Booking booking) {
        return given(spec).body(booking).post("/booking");
    }

    public Response getBooking(int id) {
        return given(spec).get("/booking/{id}", id);
    }

    public Response updateBooking(int id, Booking booking, String token) {
        return given(spec).cookie("token", token).body(booking).put("/booking/{id}", id);
    }

    public Response deleteBooking(int id, String token) {
        return given(spec).cookie("token", token).delete("/booking/{id}", id);
    }
}
