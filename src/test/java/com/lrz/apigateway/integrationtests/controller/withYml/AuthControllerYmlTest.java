/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.integrationtests.controller.withYml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lrz.apigateway.config.TestConfigs;
import com.lrz.apigateway.integrationtests.controller.withYml.mapper.YmlMapper;
import com.lrz.apigateway.integrationtests.vo.AccountCredentialsVO;
import com.lrz.apigateway.integrationtests.vo.TokenVO;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import com.lrz.apigateway.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author lara
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYmlTest extends AbstractIntegrationTest {

    private static YmlMapper mapper;
    private static TokenVO tokenVO;

    @BeforeAll
    public static void setup() {
        mapper = new YmlMapper();
    }

    @Test
    @Order(1)
    public void testSignin() throws JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        RequestSpecification specification = new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        tokenVO
                = given().spec(specification)
                        .config(RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                        .accept(TestConfigs.CONTENT_TYPE_YML)
                        .basePath("auth/signin")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .body(user, mapper)
                        .when().post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenVO.class, mapper);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }

    @Test
    @Order(2)
    public void testRefresh() throws JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        var newTokenVO
                = given()
                        .config(RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML,
                                                ContentType.TEXT)))
                        .accept(TestConfigs.CONTENT_TYPE_YML)
                        .basePath("auth/refresh")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .pathParam("username", tokenVO.getUsername())
                        .headers(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                        .when().put("{username}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenVO.class, mapper);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }

}
