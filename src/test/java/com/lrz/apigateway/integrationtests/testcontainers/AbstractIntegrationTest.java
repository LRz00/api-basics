 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.integrationtests.testcontainers;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;
/**
 *
 * @author lara
 */
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static  class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{
         static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.36");
        
        private static void startContainers(){
            Startables.deepStart(Stream.of(mysql.withStartupTimeoutSeconds(1000))).join();
        }
        
        
        private static Map<String, String> createConnectionConfiguration(){
            return Map.of(
            "spring.datasource.url", mysql.getJdbcUrl(),
            "spring.datasource.username", mysql.getUsername(),
            "spring.datasource.password", mysql.getPassword()
            );
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment enviroment = applicationContext.getEnvironment();
            MapPropertySource testcontainers = new MapPropertySource("testcontainers", 
            (Map)createConnectionConfiguration());
            enviroment.getPropertySources().addFirst(testcontainers);
        }
    }
       
}
