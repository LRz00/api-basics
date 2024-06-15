/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.integrationtests.controller.withYml.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 *
 * @author lara
 */
public class YmlMapper implements ObjectMapper {

    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    protected TypeFactory tf;

    public YmlMapper() {
        this.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        this.tf = TypeFactory.defaultInstance();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object deserialize(ObjectMapperDeserializationContext omdc) {
        try {
            String dataToDeserialize = omdc.getDataToDeserialize().asString();
            Class type = (Class) omdc.getType();
            return objectMapper.readValue(dataToDeserialize, tf.constructType(type));
        } catch ( JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object serialize(ObjectMapperSerializationContext omsc) {
        try {
            return objectMapper.writeValueAsString(omsc.getObjectToSerialize());
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
