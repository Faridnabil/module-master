package com.genz.master.users;

import org.apache.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@QuarkusTest
public class GetAllUserTest {

    private static final String BASE_URI = "http://localhost:8080";

    @Test
    void testGetAllUsers() {
        Response response = RestAssured.given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .when().get("/users")
                .then().log().all()
                .extract().response();

        // Assertions
        assertEquals(HttpStatus.SC_OK, response.statusCode());
        response.then()
                .body("status", equalTo(HttpStatus.SC_OK))
                .body("success", equalTo(true))
                .body("message", equalTo("Users retrieved successfully"))
                .body("data", not(empty()));
    }
}
