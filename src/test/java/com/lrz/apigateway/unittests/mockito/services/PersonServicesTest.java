/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.lrz.apigateway.unittests.mockito.services;

import com.lrz.apigateway.data.vo.v1.PersonVO;
import com.lrz.apigateway.data.vo.v2.PersonVOV2;
import com.lrz.apigateway.model.Person;
import com.lrz.apigateway.repository.PersonRepository;
import com.lrz.apigateway.services.PersonServices;
import com.lrz.apigateway.unittests.mapper.mocks.MockPerson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
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

        fail("The test case is a prototype.");
    }

    /**
     * Test of findAll method, of class PersonServices.
     */
    @Test
    public void testFindAll() {
        fail("The test case is a prototype.");
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
        System.out.println("update");
        PersonVO person = null;
        PersonServices instance = new PersonServices();
        PersonVO expResult = null;
        PersonVO result = instance.update(person);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class PersonServices.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        Long id = null;
        PersonServices instance = new PersonServices();
        instance.delete(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createV2 method, of class PersonServices.
     */
    @Test
    public void testCreateV2() {
        System.out.println("createV2");
        PersonVOV2 person = null;
        PersonServices instance = new PersonServices();
        PersonVOV2 expResult = null;
        PersonVOV2 result = instance.createV2(person);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
