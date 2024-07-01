/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.lrz.apigateway.unittests.mockito.services;

import com.lrz.apigateway.data.vo.v1.PersonVO;
import com.lrz.apigateway.exception.RequiredObjectIsNullException;
import com.lrz.apigateway.model.Person;
import com.lrz.apigateway.repository.PersonRepository;
import com.lrz.apigateway.services.PersonServices;
import com.lrz.apigateway.unittests.mapper.mocks.MockPerson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author lara
 */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {
    
    MockPerson input;
    
    @InjectMocks
    private PersonServices service;
    
    @Mock
    PersonRepository repository;
    
    @BeforeEach
    void setUpMocks() throws Exception {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test of create method, of class PersonServices.
     */
    @Test
    public void testCreate() {
        Person entity = input.mockEntity(1);
        
        Person persisted = entity;
        persisted.setId(1L);
        
        PersonVO vo = input.mockVO(1);
        
        vo.setKey(1L);
        
        when(repository.save(entity)).thenReturn(persisted);
        
        var result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    public void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });
        
        String expectedMessage = "Null Objects cannot be persisted";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        
    }
    /**
     * Test of findById method, of class PersonServices.
     */
    @Test
    public void testFindById() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        
        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    /**
     * Test of update method, of class PersonServices.
     */
    @Test
    public void testUpdate() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);
        
        Person persisted = entity;
        persisted.setId(1L);
        
        PersonVO vo = input.mockVO(1);
        
        vo.setKey(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);
        
        var result = service.update(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }
    
     @Test
    public void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });
        
        String expectedMessage = "Null Objects cannot be persisted";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        
    }

    /**
     * Test of delete method, of class PersonServices.
     */
    @Test
    public void testDelete() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        
        service.delete(1L);
        
    }
    
}
