/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.lrz.apigateway.unittests.mockito.services;

import com.lrz.apigateway.data.vo.v1.BookVO;
import com.lrz.apigateway.exception.RequiredObjectIsNullException;
import com.lrz.apigateway.model.Book;
import com.lrz.apigateway.repository.BookRepository;
import com.lrz.apigateway.services.BookServices;
import com.lrz.apigateway.unittests.mapper.mocks.MockBooks;


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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServicesTest {
    MockBooks input;
    
    @InjectMocks
    private BookServices service;
    
    @Mock
    BookRepository repository;
    
    @BeforeEach
    void setUpMocks() throws Exception {
        input = new MockBooks();
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test of create method, of class BookServices.
     */
    @Test
    public void testCreate() {
        Book entity = input.mockEntity(1);
        
        Book persisted = entity;
        persisted.setId(1L);
        
        BookVO vo = input.mockVO(1);
        
        vo.setKey(1L);
        
        when(repository.save(entity)).thenReturn(persisted);
        
        var result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
                assertNotNull(result.getLaunchDate());
                
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Test1", result.getAuthor());
        assertEquals(1D, result.getPrice());
        assertEquals("Title Test1", result.getTitle());

    }
    
      @Test
    public void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });
        
        String expectedMessage = "Null Objects cannot be persisted";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        
    }

    /**
     * Test of findById method, of class BookServices.
     */
    @Test
    public void testFindById() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        
        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertNotNull(result.getLaunchDate());
        
        System.out.println("****" + result.getLinks().toString());
        
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Test1", result.getAuthor());
        assertEquals(1D, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
        
    }

    /**
     * Test of findAll method, of class BookServices.
     */
    @Test
    public void testFindAll() {
       List <Book> list = input.mockEntityList();
       
        
        when(repository.findAll()).thenReturn(list);
        
        var books = service.findAll();
        assertNotNull(books);
        assertEquals(14, books.size());
        
        var bookOne = books.get(1);
        assertNotNull(bookOne);
        assertNotNull(bookOne.getKey());
        assertNotNull(bookOne.getLinks());
                assertNotNull(bookOne.getLaunchDate());
        assertTrue(bookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Test1", bookOne.getAuthor());
        assertEquals(1D, bookOne.getPrice());
        assertEquals("Title Test1", bookOne.getTitle());
        
        var bookfive = books.get(5);
        assertNotNull(bookfive);
        assertNotNull(bookfive.getKey());
        assertNotNull(bookfive.getLinks());
                assertNotNull(bookfive.getLaunchDate());
        assertTrue(bookfive.toString().contains("links: [</api/book/v1/5>;rel=\"self\"]"));
        assertEquals("Author Test5", bookfive.getAuthor());
        assertEquals(5D, bookfive.getPrice());
        assertEquals("Title Test5", bookfive.getTitle());
    }

    /**
     * Test of update method, of class BookServices.
     */
    @Test
    public void testUpdate() {
       Book entity = input.mockEntity(1);
        entity.setId(1L);
        
        Book persisted = entity;
        persisted.setId(1L);
        
        BookVO vo = input.mockVO(1);
        
        vo.setKey(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);
        
        var result = service.update(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertNotNull(result.getLaunchDate());
        
        System.out.println("****" + result.getLinks().toString());
        
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Test1", result.getAuthor());
        assertEquals(1D, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
    }
    
         @Test
    public void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });
        
        String expectedMessage = "Null Objects cannot be persisted";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        
    }

    /**
     * Test of delete method, of class BookServices.
     */
    @Test
    public void testDelete() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        
        service.delete(1L);
    }
    
}
