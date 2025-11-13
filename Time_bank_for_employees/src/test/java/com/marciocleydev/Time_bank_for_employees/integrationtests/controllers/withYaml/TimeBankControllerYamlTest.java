package com.marciocleydev.Time_bank_for_employees.integrationtests.controllers.withYaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.marciocleydev.Time_bank_for_employees.config.TestConfigs;
import com.marciocleydev.Time_bank_for_employees.integrationtests.controllers.withYaml.mapper.YAMLMapper;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.TimeBankDTO;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.security.AccountCredentialsDTO;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.security.TokenDTO;
import com.marciocleydev.Time_bank_for_employees.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.Instant;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TimeBankControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper yamlMapper;
    private static TimeBankDTO timeBankDTO;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        yamlMapper = new YAMLMapper();

        timeBankDTO = new TimeBankDTO();
        tokenDTO = new TokenDTO();
    }

    @Order(0)
    @Test
    void signin() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO();
        credentials.setUsername("marcio");
        credentials.setPassword("admin123");

        tokenDTO = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                                )
                )
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(credentials, yamlMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class, yamlMapper);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Order(1)
    @Test
    void getBalance() throws JsonProcessingException {
        setSpecification();

        var content = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                                )
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("employeeId", 2L)
                .when()
                .get("/balance")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TimeBankDTO.class, yamlMapper);

        timeBankDTO = content;
        verifyAssertNotNull();
        verifyAssertEquals(1L, 2, timeBankDTO.getLastUpdate(), 2L);
    }

    @Order(2)
    @Test
    void addHours() throws JsonProcessingException {
        var content = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                                )
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("employeeId", 2L)
                .queryParam("minutes", 60)
                .when()
                .post("/add")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TimeBankDTO.class, yamlMapper);

        timeBankDTO = content;
        verifyAssertNotNull();
        verifyAssertEquals(1L, 62, timeBankDTO.getLastUpdate(), 2L);
    }

    @Order(3)
    @Test
    void removeHours() throws JsonProcessingException {
        var content = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                                )
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("employeeId", 2L)
                .queryParam("minutes", 60)
                .when()
                .post("/remove")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TimeBankDTO.class, yamlMapper);

        timeBankDTO = content;
        verifyAssertNotNull();
        verifyAssertEquals(1L, 2, timeBankDTO.getLastUpdate(), 2L);
    }

    private void setSpecification() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MARCIOCLEY)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/employees/{employeeId}/timeBank")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    private void verifyAssertEquals(Long id, Integer  totalValue, Instant lastUpdate, Long employeeId) {
        assertAll(
                () -> assertEquals(timeBankDTO.getId(), id),
                () -> assertEquals(timeBankDTO.getTotalValue(), totalValue),
                () -> assertEquals(timeBankDTO.getLastUpdate(), lastUpdate),
                () -> assertEquals(timeBankDTO.getEmployeeId(), employeeId)
        );
    }

    private void verifyAssertNotNull() {
        assertAll(
                () -> assertNotNull(timeBankDTO.getId()),
                () -> assertNotNull(timeBankDTO.getTotalValue()),
                () -> assertNotNull(timeBankDTO.getLastUpdate()),
                () -> assertNotNull(timeBankDTO.getEmployeeId()),
                () -> assertTrue(timeBankDTO.getId() > 0)
        );
    }

}