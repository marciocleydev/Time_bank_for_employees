/*
package com.marciocleydev.Time_bank_for_employees.integrationtests.controllers.withJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marciocleydev.Time_bank_for_employees.config.TestConfigs;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.TimeBankDTO;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.security.AccountCredentialsDTO;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.security.TokenDTO;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.wrappers.WrapperTimeBankDTO;
import com.marciocleydev.Time_bank_for_employees.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TimeBankControllerJsonTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static TimeBankDTO timeBankDTO;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        timeBankDTO = new TimeBankDTO();

        tokenDTO = new TokenDTO();
    }

    @Order(0)
    @Test
    void signin() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO();
        credentials.setUsername("marcio");
        credentials.setPassword("admin123");

        tokenDTO = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Order(2)
    @Test
    void findById() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", timeBankDTO.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        timeBankDTO = objectMapper.readValue(content, TimeBankDTO.class);
        verifyAssertNotNull();
        verifyAssertEquals(1);
    }

    @Order(3)
    @Test
    void update() throws JsonProcessingException {
        mockTimeBank(2);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", timeBankDTO.getId())
                .body(timeBankDTO)
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        timeBankDTO = objectMapper.readValue(content, TimeBankDTO.class);
        verifyAssertNotNull();
        verifyAssertEquals(2);
    }

    @Order(4)
    @Test
    void deleteById() {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", timeBankDTO.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(204)
                .extract()
                .body()
                .asString();

        assertEquals("", content);
    }

    @Order(5)
    @Test
    void findAll() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 0, "size", 11, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperTimeBankDTO wrapper = objectMapper.readValue(content, WrapperTimeBankDTO.class);
        List<TimeBankDTO> timeBanks = wrapper.getEmbedded().getTimeBanks();
        timeBankDTO = timeBanks.getFirst();
        verifyAssertNotNull();
        assertAll(
                () -> assertEquals(92, timeBankDTO.getId()),
                () -> assertEquals("Aarika", timeBankDTO.getName()),
                () -> assertEquals("157-85-2459", timeBankDTO.getPis()),
                () -> assertEquals(11, timeBanks.size())
        );

        timeBankDTO = timeBanks.get(6);
        verifyAssertNotNull();
        assertAll(
                () -> assertEquals(35, timeBankDTO.getId()),
                () -> assertEquals("Artair", timeBankDTO.getName()),
                () -> assertEquals("507-93-9859", timeBankDTO.getPis())
        );
    }

    private void verifyAssertEquals(Integer n) {
        assertAll(
                () -> assertEquals(timeBankDTO.getName(), "Leonardo " + n),
                () -> assertEquals(timeBankDTO.getPis(), "123456789" + n)
        );
    }

    private void setSpecification() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MARCIOCLEY)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/timeBanks")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    private void verifyAssertNotNull() {
        assertAll(
                () -> assertNotNull(timeBankDTO.getId()),
                () -> assertNotNull(timeBankDTO.getName()),
                () -> assertNotNull(timeBankDTO.getPis()),
                () -> assertTrue(timeBankDTO.getId() > 0)
        );
    }
}*/
