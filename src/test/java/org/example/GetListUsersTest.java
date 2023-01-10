package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.util.Lists;
import org.example.dto.Data;
import org.example.dto.Users;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;


public class GetListUsersTest {
    private static ValidatableResponse data;

    private static Users users;
    private static final int EXPECTED_TOTAL = 12;
    private static final List<String> EXPECTED_LAST_NAMES = Lists.newArrayList("Lawson", "Ferguson");

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        data = when().
                get("/api/users?page=2").
                then().
                contentType(ContentType.JSON).
                statusCode(200);
        users = data.extract().body().as(Users.class);
    }

    @Test
    public void totalFieldTest() {
        Integer total = users.getTotal();
        assertThat(total).isNotNull();
        assertThat(total).isEqualTo(EXPECTED_TOTAL);
    }

    @Test
    public void lastNamesTest() {
        List<String> firstTwoLastNames = users.getData().stream()
                .map(Data::getLastName)
                .limit(2)
                .toList();

        assertThat(firstTwoLastNames).containsExactlyElementsOf(EXPECTED_LAST_NAMES);
    }

    @Test
    public void totalFieldRepresentsActualTotalCountOfUSersTest() {
        Integer total = users.getTotal();
        assertThat(total).isEqualTo(users.getData().size());
    }

    @AfterAll
    static void teardown() {
        RestAssured.reset();
        data = null;
    }

}
