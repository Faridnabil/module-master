package com.genz.master.users;

import org.apache.http.HttpStatus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import com.genz.master.modules.users.dto.UserRequestDto;

@QuarkusTest
public class PostCreateUserTest {

    private static final String BASE_URI = "http://localhost:8080";

    @Test
    void testInvalidPayload() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/users")
                .then().log().all().statusCode(HttpStatus.SC_OK)
                .extract().response().then().body("status", equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    void testCreateUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("John Doe");
        userRequestDto.setEmail("john.doe@example.com");
        userRequestDto.setPassword("password");

        Response response = RestAssured.given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body(userRequestDto)
                .when().post("/users")
                .then().log().all()
                .extract().response();

        assertEquals(HttpStatus.SC_OK, response.statusCode());
        response.then()
                .body("status", equalTo(HttpStatus.SC_OK))
                .body("success", equalTo(true))
                .body("message", equalTo("User created successfully"))
                .body("data", not(empty()));
    }
}
