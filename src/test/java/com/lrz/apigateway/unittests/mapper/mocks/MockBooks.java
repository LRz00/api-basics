/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.unittests.mapper.mocks;

import com.lrz.apigateway.data.vo.v1.BookVO;
import com.lrz.apigateway.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lara
 */
public class MockBooks {
     public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookVO mockVO() {
        return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
            book.setAuthor("Author Test" + number);
        book.setTitle("Title Test" + number);
        book.setPrice(number.doubleValue());
        book.setId(number.longValue());
        book.setLaunchDate(new Date());
        return book;
    }

    public BookVO mockVO(Integer number) {
        BookVO book = new BookVO();
        book.setAuthor("Author Test" + number);
        book.setTitle("Title Test" + number);
        book.setPrice(number.doubleValue());
        book.setKey(number.longValue());
        book.setLaunchDate(new Date());
        return book;
    }
}

