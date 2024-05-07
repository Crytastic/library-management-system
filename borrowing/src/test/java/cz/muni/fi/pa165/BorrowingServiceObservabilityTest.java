package cz.muni.fi.pa165;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * Taken from seminar-observability
 *
 * @author Martin Štefanko
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BorrowingServiceObservabilityTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void beforeEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void exposedActuatorEndpoints() {
        given()
                .when()
                .get("/actuator")
                .then()
                .statusCode(200)
                .body("_links.beans", nullValue())
                .body("_links.caches", nullValue())
                .body("_links.caches-cache", nullValue())
                .body("_links.conditions", nullValue())
                .body("_links.configprops", nullValue())
                .body("_links.configprops-prefix", nullValue())
                .body("_links.env", nullValue())
                .body("_links.env-toMatch", nullValue())
                .body("_links.health", notNullValue())
                .body("_links.health-path", notNullValue())
                .body("_links.heapdump", nullValue())
                .body("_links.info", notNullValue())
                .body("_links.loggers", notNullValue())
                .body("_links.loggers-name", notNullValue())
                .body("_links.mappings", nullValue())
                .body("_links.metrics", notNullValue())
                .body("_links.metrics-requiredMetricName", notNullValue())
                .body("_links.scheduledtasks", nullValue())
                .body("_links.self", notNullValue())
                .body("_links.threaddump", nullValue());
    }

    @Test
    void prometheusMetricsExported() {
        given()
                .when()
                .get("/actuator/prometheus")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }
}
