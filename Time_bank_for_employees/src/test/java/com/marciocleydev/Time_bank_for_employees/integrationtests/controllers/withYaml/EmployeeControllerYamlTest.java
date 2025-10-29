package com.marciocleydev.Time_bank_for_employees.integrationtests.controllers.withYaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.marciocleydev.Time_bank_for_employees.config.TestConfigs;
import com.marciocleydev.Time_bank_for_employees.integrationtests.controllers.withYaml.mapper.YAMLMapper;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.EmployeeDTO;
import com.marciocleydev.Time_bank_for_employees.integrationtests.testcontainers.AbstractIntegrationTest;
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

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeControllerYamlTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static YAMLMapper yamlMapper;
    private static EmployeeDTO employeeDTO;

    @BeforeAll
    static void setUp() {
        yamlMapper = new YAMLMapper();

        employeeDTO = new EmployeeDTO();
    }

    @Order(1)
    @Test
    void create() throws JsonProcessingException {
        mockEmployee(1);
        setSpecification();

        var content = given().config(
                RestAssuredConfig.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                        )
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(employeeDTO, yamlMapper)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(EmployeeDTO.class, yamlMapper);

        employeeDTO = content;
        verifyAssertNotNull();
        verifyAssertEquals(1);
    }

    @Order(2)
    @Test
    void findById() throws JsonProcessingException {
        var content = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                                )
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", employeeDTO.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(EmployeeDTO.class, yamlMapper);

        employeeDTO = content;
        verifyAssertNotNull();
        verifyAssertEquals(1);
    }

    @Order(3)
    @Test
    void update() throws JsonProcessingException {
        mockEmployee(2);

        var content = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                                )
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", employeeDTO.getId())
                .body(employeeDTO, yamlMapper)
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(EmployeeDTO.class, yamlMapper);

        employeeDTO = content;
        verifyAssertNotNull();
        verifyAssertEquals(2);
    }

    @Order(4)
    @Test
    void deleteById() {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
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
        var content = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                                )
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(EmployeeDTO[].class, yamlMapper);

            var employees = Arrays.asList(content);
            employeeDTO = employees.getFirst();
            verifyAssertNotNull();
            assertAll(
                    () -> assertEquals(2, employeeDTO.getId()),
                    () ->assertEquals("Caralie", employeeDTO.getName()),
                    () -> assertEquals("767-83-0693", employeeDTO.getPis()),
                    () -> assertEquals(100, employees.size())
            );

        employeeDTO = employees.get(47);
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