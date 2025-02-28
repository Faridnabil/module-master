package com.genz.master.users;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.ws.rs.core.MediaType;

import org.hamcrest.Matchers;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class GetUserByNameTest {

    private static final String BASE_URI = "http://localhost:8080";

    @Test
    void testInvalidPayload() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/users/{name}")
                .then().log().all().statusCode(HttpStatus.SC_OK)
                .extract().response().then().body("status", Matchers.equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    void testGetUserByName() {
        Response response = RestAssured.given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .queryParam("name", "John Doe")
                .when().get("/users/{name}")
                .then().log().all()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
        response.then()
                .body("status", Matchers.equalTo(HttpStatus.SC_OK))
                .body("success", Matchers.equalTo(true))
                .body("message", Matchers.equalTo("User found"))
                .body("data", Matchers.not(Matchers.empty()));
    }
}
