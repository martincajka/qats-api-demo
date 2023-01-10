package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.dto.CreateUser;
import org.example.dto.CreateUserResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


public class PostCreateNewUserTest {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv", numLinesToSkip = 1)
    public void totalFieldTest(String name, String job) {
//        create and send request for new user creation
        CreateUser user = new CreateUser(name, job);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/users")
                .then().extract().response();

//        get year when user was created
        CreateUserResponse createUserResponse = response.as(CreateUserResponse.class);
        int createdAtYear = createUserResponse.getCreatedAt().atZone(ZoneId.systemDefault()).getYear();

//        validations
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(createdAtYear).isEqualTo(ZonedDateTime.now().getYear());
        assertThat(response.timeIn(TimeUnit.MILLISECONDS)).isLessThan(100L);
    }

    @AfterAll
    static void teardown() {
        RestAssured.reset();
    }

}
