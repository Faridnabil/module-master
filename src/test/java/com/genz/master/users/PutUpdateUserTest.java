package com.genz.master.users;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.ws.rs.core.MediaType;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import com.genz.master.modules.users.dto.UserRequestDto;

@QuarkusTest
public class PutUpdateUserTest {

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
    void testUpdateUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername("John Doe Updated");
        userRequestDto.setEmail("john.doe.updated@example.com");
        userRequestDto.setPassword("newpassword");

        Response response = RestAssured.given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .queryParam("id", 1)
                .body(userRequestDto)
                .when().put("/users/{id}")
                .then().log().all()
                .extract().response();

        // Assertions
        assertEquals(HttpStatus.SC_OK, response.statusCode());
        response.then()
                .body("status", equalTo(HttpStatus.SC_OK))
                .body("success", equalTo(true))
                .body("message", equalTo("User updated successfully"))
                .body("data", not(empty()));
    }

}
