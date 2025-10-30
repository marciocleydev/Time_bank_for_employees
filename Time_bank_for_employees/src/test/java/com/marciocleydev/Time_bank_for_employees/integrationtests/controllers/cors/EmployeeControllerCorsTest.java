package com.marciocleydev.Time_bank_for_employees.integrationtests.controllers.cors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.EmployeeDTO;
import com.marciocleydev.Time_bank_for_employees.config.TestConfigs;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.wrappers.WrapperEmployeeDTO;
import com.marciocleydev.Time_bank_for_employees.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeControllerCorsTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static EmployeeDTO employeeDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

         employeeDTO = new EmployeeDTO();
    }

    @Order(1)
    @ParameterizedTest
    @CsvSource({
            TestConfigs.ORIGIN_MARCIOCLEY + ", 201",
            TestConfigs.ORIGIN_GOOGLE + ", 403"
    })
    void create(String origin, int expectedStatus) throws JsonProcessingException {
        mockEmployee(1);
        setSpecification(origin);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(employeeDTO)
                .when()
                .post()
                .then()
                .statusCode(expectedStatus)
                .extract()
                .body()
                .asString();

        if(expectedStatus == 201){
            employeeDTO = objectMapper.readValue(content, EmployeeDTO.class);
            verifyAssertNotNull();
            verifyAssertEquals(1);
        }
        else{
            Assertions.assertEquals("Invalid CORS request", content);
        }
    }

    @Order(2)
    @ParameterizedTest
    @CsvSource({
            TestConfigs.ORIGIN_MARCIOCLEY + ", 200",
            TestConfigs.ORIGIN_GOOGLE + ", 403"
    })
    void findById(String origin, int expectedStatus) throws JsonProcessingException {
        setSpecification(origin);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", employeeDTO.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(expectedStatus)
                .extract()
                .body()
                .asString();

        if(expectedStatus == 200){
            employeeDTO = objectMapper.readValue(content, EmployeeDTO.class);
            verifyAssertNotNull();
            verifyAssertEquals(1);
        }
        else{
            Assertions.assertEquals("Invalid CORS request", content);
        }
    }

    @Order(3)
    @ParameterizedTest
    @CsvSource({
            TestConfigs.ORIGIN_MARCIOCLEY + ", 200",
            TestConfigs.ORIGIN_GOOGLE + ", 403"
    })
    void update(String origin, int expectedStatus) throws JsonProcessingException {
        setSpecification(origin);
        mockEmployee(2);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", employeeDTO.getId())
                .body(employeeDTO)
                .when()
                .put("/{id}")
                .then()
                .statusCode(expectedStatus)
                .extract()
                .body()
                .asString();

        if(expectedStatus == 200){
            employeeDTO = objectMapper.readValue(content, EmployeeDTO.class);
            verifyAssertNotNull();
            verifyAssertEquals(2);
        }
        else{
            Assertions.assertEquals("Invalid CORS request", content);
        }
    }

    @Order(4)
    @ParameterizedTest
    @CsvSource({
            TestConfigs.ORIGIN_MARCIOCLEY + ", 204",
            TestConfigs.ORIGIN_GOOGLE + ", 403"
    })
    void deleteById(String origin, int expectedStatus) {
        setSpecification(origin);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", employeeDTO.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(expectedStatus)
                .extract()
                .body()
                .asString();

        if(expectedStatus == 204){
            assertEquals("", content);
        }
        else{
            assertEquals("Invalid CORS request", content);
        }

    }

    @Order(5)
    @ParameterizedTest
    @CsvSource({
            TestConfigs.ORIGIN_MARCIOCLEY + ", 200",
            TestConfigs.ORIGIN_GOOGLE + ", 403"
    })
    void findAll(String origin, int expectedStatus) throws JsonProcessingException {
        setSpecification(origin);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 0, "size", 11, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(expectedStatus)
                .extract()
                .body()
                .asString();

        if(expectedStatus == 200){
            WrapperEmployeeDTO wrapper = objectMapper.readValue(content, WrapperEmployeeDTO.class);
            List<EmployeeDTO> employees = wrapper.getEmbedded().getEmployees();
            employeeDTO = employees.getFirst();
            verifyAssertNotNull();
            assertAll(
                    () -> assertEquals(92, employeeDTO.getId()),
                    () -> assertEquals("Aarika", employeeDTO.getName()),
                    () -> assertEquals("157-85-2459", employeeDTO.getPis()),
                    () -> assertEquals(11, employees.size())
            );
        }
        else{
            assertEquals("Invalid CORS request", content);
        }
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

    private void setSpecification(String origin) {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, origin)
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