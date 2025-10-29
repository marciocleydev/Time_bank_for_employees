package com.marciocleydev.Time_bank_for_employees.integrationtests.controllers.withXml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.marciocleydev.Time_bank_for_employees.config.TestConfigs;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.EmployeeDTO;
import com.marciocleydev.Time_bank_for_employees.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeControllerXmlTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static XmlMapper xmlMapper;
    private static EmployeeDTO employeeDTO;

    @BeforeAll
    static void setUp() {
        xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        employeeDTO = new EmployeeDTO();
    }

    @Order(1)
    @Test
    void create() throws JsonProcessingException {
        mockEmployee(1);
        setSpecification();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(employeeDTO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        employeeDTO = xmlMapper.readValue(content, EmployeeDTO.class);
        verifyAssertNotNull();
        verifyAssertEquals(1);
    }

    @Order(2)
    @Test
    void findById() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", employeeDTO.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        employeeDTO = xmlMapper.readValue(content, EmployeeDTO.class);
        verifyAssertNotNull();
        verifyAssertEquals(1);
    }

    @Order(3)
    @Test
    void update() throws JsonProcessingException {
        mockEmployee(2);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", employeeDTO.getId())
                .body(employeeDTO)
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        employeeDTO = xmlMapper.readValue(content, EmployeeDTO.class);
        verifyAssertNotNull();
        verifyAssertEquals(2);
    }

    @Order(4)
    @Test
    void deleteById() {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", employeeDTO.getId())
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
                .accept(MediaType.APPLICATION_XML_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

            var employees = xmlMapper.readValue(content, EmployeeDTO[].class);
            employeeDTO = employees[0];
            verifyAssertNotNull();
            assertAll(
                    () -> assertEquals(2, employeeDTO.getId()),
                    () ->assertEquals("Caralie", employeeDTO.getName()),
                    () -> assertEquals("767-83-0693", employeeDTO.getPis()),
                    () -> assertEquals(100, employees.length)
            );

        employeeDTO = employees[47];
        verifyAssertNotNull();
        assertAll(
                () -> assertEquals(49, employeeDTO.getId()),
                () ->assertEquals("Margit", employeeDTO.getName()),
                () -> assertEquals("130-55-3113", employeeDTO.getPis())
        );
    }

    private void mockEmployee(Integer n) {
        employeeDTO.setName("Leonardo "+ n);
        employeeDTO.setPis("123456789"+n);
    }

    private void verifyAssertEquals(Integer n) {
        assertAll(
                () -> assertEquals(employeeDTO.getName(), "Leonardo "+ n),
                () ->  assertEquals(employeeDTO.getPis(), "123456789"+n)
        );
    }

    private void setSpecification() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MARCIOCLEY)
                .setBasePath("/employees")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    private void verifyAssertNotNull() {
        assertAll(
                () -> assertNotNull(employeeDTO.getId()),
                () -> assertNotNull(employeeDTO.getName()),
                () -> assertNotNull(employeeDTO.getPis()),
                () -> assertTrue(employeeDTO.getId() > 0)
        );
    }
}