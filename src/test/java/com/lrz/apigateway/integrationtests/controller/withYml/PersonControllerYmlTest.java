package com.lrz.apigateway.integrationtests.controller.withYml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lrz.apigateway.integrationtests.testcontainers.AbstractIntegrationTest;
import com.lrz.apigateway.integrationtests.vo.PersonVO;
import com.lrz.apigateway.config.TestConfigs;
import com.lrz.apigateway.integrationtests.vo.AccountCredentialsVO;
import com.lrz.apigateway.integrationtests.vo.TokenVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lrz.apigateway.integrationtests.controller.withYml.mapper.YmlMapper;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerYmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static PersonVO person;
    private static YmlMapper objectMapper;

    @BeforeAll
    private static void setup() {
        objectMapper = new YmlMapper();
        person = new PersonVO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        var acessToken
                = given().config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfigs.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                        .basePath("auth/signin")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .body(user, objectMapper)
                        .when().post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenVO.class, objectMapper).getAccessToken();

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
                        .config(
                                RestAssuredConfig
                                        .config()
                                        .encoderConfig(EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(
                                                        TestConfigs.CONTENT_TYPE_YML,
                                                        ContentType.TEXT)))
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .body(person, objectMapper)
                        .when().post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(PersonVO.class, objectMapper);

        PersonVO createdPerson = content;

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("PedruLino", createdPerson.getAddress());
        assertEquals("Juan", createdPerson.getFirstName());
        assertEquals("Mendes", createdPerson.getLastName());
        assertEquals("boy", createdPerson.getGender());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonProcessingException {
        person.setLastName("Rodrigues");
        var content
                = given()
                        .spec(specification)
                        .config(
                                RestAssuredConfig
                                        .config()
                                        .encoderConfig(EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(
                                                        TestConfigs.CONTENT_TYPE_YML,
                                                        ContentType.TEXT)))
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .body(person, objectMapper)
                        .when().post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(PersonVO.class, objectMapper);

        PersonVO createdPerson = content;

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
        assertEquals(person.getId(), createdPerson.getId());

        assertEquals("PedruLino", createdPerson.getAddress());
        assertEquals("Juan", createdPerson.getFirstName());
        assertEquals("Rodrigues", createdPerson.getLastName());
        assertEquals("boy", createdPerson.getGender());
    }

    @Test
    @Order(3)
    public void testFindById() throws JsonProcessingException {
        mockPerson();

        var content
                = given()
                        .spec(specification)
                        .config(
                                RestAssuredConfig
                                        .config()
                                        .encoderConfig(EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(
                                                        TestConfigs.CONTENT_TYPE_YML,
                                                        ContentType.TEXT)))
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_URL)
                        .pathParam("id", person.getId())
                        .when().get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(PersonVO.class, objectMapper);

        PersonVO createdPerson = content;

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("PedruLino", createdPerson.getAddress());
        assertEquals("Juan", createdPerson.getFirstName());
        assertEquals("Rodrigues", createdPerson.getLastName());
        assertEquals("boy", createdPerson.getGender());
    }

    @Test
    @Order(4)
    public void testDelete() throws JsonProcessingException {
        given()
                .spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfigs.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", person.getId())
                .when().delete("{id}")
                .then()
                .statusCode(204);

    }
    
    /* NOT WORKING
     @Test
    @Order(5)
    public void testDisable() throws JsonProcessingException {
        System.out.println("Person id: " +person.getId().toString());
        var content
                = given().spec(specification)
                        .config(
                                RestAssuredConfig
                                        .config()
                                        .encoderConfig(EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(
                                                        TestConfigs.CONTENT_TYPE_YML,
                                                        ContentType.TEXT)))
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .accept(TestConfigs.CONTENT_TYPE_YML)
                        .pathParam("id", person.getId())
                        .when()
                        .patch("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(PersonVO.class, objectMapper);

        PersonVO createdPerson = content;

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("PedruLino", createdPerson.getAddress());
        assertEquals("Juan", createdPerson.getFirstName());
        assertEquals("Rodrigues", createdPerson.getLastName());
        assertEquals("boy", createdPerson.getGender());
    }
    */
   

    @Test
    @Order(6)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfigs.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO[].class, objectMapper);

        List<PersonVO> people = Arrays.asList(content);

        PersonVO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());

        assertEquals(1, foundPersonOne.getId());

        assertEquals("Ayrton", foundPersonOne.getFirstName());
        assertEquals("Senna", foundPersonOne.getLastName());
        assertEquals("São Paulo", foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());

        PersonVO foundPersonSix = people.get(5);

        assertNotNull(foundPersonSix.getId());
        assertNotNull(foundPersonSix.getFirstName());
        assertNotNull(foundPersonSix.getLastName());
        assertNotNull(foundPersonSix.getAddress());
        assertNotNull(foundPersonSix.getGender());

        assertEquals(9, foundPersonSix.getId());

        assertEquals("Nelson", foundPersonSix.getFirstName());
        assertEquals("Mvezo", foundPersonSix.getLastName());
        assertEquals("Mvezo – South Africa", foundPersonSix.getAddress());
        assertEquals("Male", foundPersonSix.getGender());
    }

    @Test
    @Order(7)
    public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specificationWithoutToken)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                TestConfigs.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    private void mockPerson() {
        person.setFirstName("Juan");
        person.setLastName("Mendes");
        person.setAddress("PedruLino");
        person.setGender("boy");
        person.setEnabled(true);
    }

}
