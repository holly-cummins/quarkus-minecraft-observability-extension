package org.acme.minecrafter.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MinecrafterResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/minecrafter")
                .then()
                .statusCode(200)
                .body(is("Hello minecrafter"));
    }
}
