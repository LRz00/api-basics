package com.lrz.apigateway.integrationtests.controller.cors.withJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrz.apigateway.config.TestConfigs;
import com.lrz.apigateway.integrationtests.testcontainers.AbstractIntegrationTest;
import com.lrz.apigateway.integrationtests.vo.PersonVO;
import com.lrz.apigateway.config.TestConfigs;
import com.lrz.apigateway.integrationtests.vo.AccountCredentialsVO;
import com.lrz.apigateway.integrationtests.vo.TokenVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerCorsJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonVO person;

    @BeforeAll
    private static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonVO();
    }

    @Test
    @Order(0)
    public void authorization() throws  JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        var acessToken
                = given()
                        .basePath("auth/signin")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(user)
                        .when().post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenVO.class).getAccessToken();

        specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + acessToken)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testCreate() throws JsonProcessingException {
        mockPerson();
        var content
                = given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_URL)
                        .body(person)
                        .when().post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();
        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getGender());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Scotland", createdPerson.getAddress());
        assertEquals("Ciara", createdPerson.getFirstName());
        assertEquals("Delaney", createdPerson.getLastName());
        assertEquals("gal", createdPerson.getGender());
    }

    @Test
    @Order(2)
    public void testCreateWithWrongOrigin() throws JsonProcessingException {
        mockPerson();
        var content
                = given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FAUX_URL)
                        .body(person)
                        .when().post()
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    public void testFindById() throws JsonProcessingException {
        mockPerson();

        var content
                = given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_URL)
                        .pathParam("id", person.getId())
                        .when().get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();
        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getGender());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Scotland", createdPerson.getAddress());
        assertEquals("Ciara", createdPerson.getFirstName());
        assertEquals("Delaney", createdPerson.getLastName());
        assertEquals("gal", createdPerson.getGender());
    }

    @Test
    @Order(4)
    public void testFindByIdWithWrongOrigin() throws JsonProcessingException {
        mockPerson();

        var content
                = given()
                        .spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FAUX_URL)
                        .pathParam("id", person.getId())
                        .when().get("{id}")
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    private void mockPerson() {
        person.setFirstName("Ciara");
        person.setLastName("Delaney");
        person.setAddress("Scotland");
        person.setGender("gal");
    }

}
