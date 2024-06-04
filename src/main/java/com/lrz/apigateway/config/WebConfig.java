/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.config;

import com.lrz.apigateway.serialization.converter.YamlJacksonToHttpMessageConverter;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 *
 * @author lara
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-yaml");
    
    @Value("${cors.originPatterns:default}")
    private String corsOriginPatterns = "";
             
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new YamlJacksonToHttpMessageConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
       var allowedOrigins = corsOriginPatterns.split(",");
       registry.addMapping("/**")
//               .allowedMethods("GET", "POST, "PUT")
               .allowedMethods("*")
               .allowedOrigins(allowedOrigins)
               .allowCredentials(true);
    }
    
    

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        /*Content negotiation via query params:
        
        configurer.favorParameter(true).parameterName("mediaType")
                .ignoreAcceptHeader(true)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML); 
        */
        
        //Via header:
        configurer.favorParameter(false)
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YML);
    }
    
}
