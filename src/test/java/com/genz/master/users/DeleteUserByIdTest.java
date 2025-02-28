package com.genz.master.users;

import static org.hamcrest.Matchers.equalTo;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
class DeleteUserByIdTest {

    private static final String BASE_URI = "http://localhost:8080";

    @Test
    void testInvalidPayload() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/users/{id}")
                .then().log().all().statusCode(HttpStatus.SC_OK)
                .extract().response().then().body("status", equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    void testDeleteUser() {
        Response response = RestAssured.given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .queryParam("id", 1)
                .when().delete("/users/{id}")
                .then().log().all()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
        response.then()
                .body("status", Matchers.equalTo(HttpStatus.SC_OK))
                .body("success", Matchers.equalTo(true))
                .body("message", equalTo("User deleted successfully"));
    }
}
