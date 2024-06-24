/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.services;

import com.lrz.apigateway.controller.BookController;
import com.lrz.apigateway.controller.PersonController;
import com.lrz.apigateway.data.vo.v1.BookVO;
import com.lrz.apigateway.data.vo.v1.PersonVO;
import com.lrz.apigateway.exception.RequiredObjectIsNullException;
import com.lrz.apigateway.exception.ResourceNotFoundException;
import com.lrz.apigateway.mapper.DozerMapper;
import com.lrz.apigateway.model.Book;
import com.lrz.apigateway.repository.BookRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

/**
 *
 * @author lara
 */
@Service
public class BookServices {

    @Autowired
    BookRepository repository;
    
    @Autowired
    PagedResourcesAssembler<BookVO> assembler; 

    Logger logger = Logger.getLogger(BookServices.class.getName());

    public BookVO create(BookVO book) {
        if (book == null) {
            throw new RequiredObjectIsNullException();
        }
        logger.info("Saving new Book");
        var entity = DozerMapper.parseObject(book, Book.class);
        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public BookVO findById(Long id) {
        logger.log(Level.INFO, "Finding book of id: {0}", id.toString());

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        var vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());

        return vo;
    }

    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {
        logger.info("finding all books");

        var bookPage = repository.findAll(pageable);
        var bookVosPage = bookPage.map( p -> DozerMapper.parseObject(p, BookVO.class));
        
        bookVosPage.map(p -> p.add(
                linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
        
        Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), 
                pageable.getPageSize(), "asc")).withSelfRel();
        
        return assembler.toModel(bookVosPage, link);
    }

    public BookVO update(BookVO book) {

        if (book == null) {
            throw new RequiredObjectIsNullException();
        }

        logger.log(Level.INFO, "Updating Book of id:{0}", book.getKey().toString());

        Book entity = repository.findById(book.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Key"));

        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());

        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public void delete(Long id) {
        logger.log(Level.INFO, "Deleting book of id:{0}", id.toString());
        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        repository.delete(entity);
    }
}
