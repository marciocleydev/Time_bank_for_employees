package com.marciocleydev.Time_bank_for_employees.integrationtests.controllers.withXml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.marciocleydev.Time_bank_for_employees.config.TestConfigs;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.TimeBankDTO;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.security.AccountCredentialsDTO;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.security.TokenDTO;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TimeBankControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper xmlMapper;
    private static TimeBankDTO timeBankDTO;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        xmlMapper.registerModule(new JavaTimeModule());

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
                .contentType(MediaType.APPLICATION_XML_VALUE)
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

    @Order(1)
    @Test
    void getBalance() throws JsonProcessingException {
        setSpecification();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("employeeId", 2L)
                .when()
                .get("/balance")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        timeBankDTO = xmlMapper.readValue(content, TimeBankDTO.class);
        verifyAssertNotNull();
        verifyAssertEquals(1L, 2,timeBankDTO.getLastUpdate(), 2L);
    }

    @Order(2)
    @Test
    void addHours() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("employeeId", 2L)
                .queryParam("minutes", 60)
                .when()
                .post("/add")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        timeBankDTO = xmlMapper.readValue(content, TimeBankDTO.class);
        verifyAssertNotNull();
        verifyAssertEquals(1L, 62, timeBankDTO.getLastUpdate(), 2L);
    }

    @Order(3)
    @Test
    void removeHours() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("employeeId", 2L)
                .queryParam("minutes", 60)
                .when()
                .post("/remove")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        timeBankDTO = xmlMapper.readValue(content, TimeBankDTO.class);
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