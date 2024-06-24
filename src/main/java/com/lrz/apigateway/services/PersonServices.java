package com.lrz.apigateway.services;

import com.lrz.apigateway.controller.PersonController;
import com.lrz.apigateway.data.vo.v1.PersonVO;
import com.lrz.apigateway.data.vo.v2.PersonVOV2;
import com.lrz.apigateway.exception.RequiredObjectIsNullException;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.lrz.apigateway.exception.ResourceNotFoundException;
import com.lrz.apigateway.mapper.DozerMapper;
import com.lrz.apigateway.mapper.custom.PersonMapper;
import com.lrz.apigateway.model.Person;
import com.lrz.apigateway.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.logging.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

@Service
public class PersonServices {

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper mapper;

    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;        
    
    Logger logger = Logger.getLogger(PersonServices.class.getName());

    public PersonVO create(PersonVO person) {

        if (person == null) {
            throw new RequiredObjectIsNullException();
        }

        logger.info("Creating person");
        var entity = DozerMapper.parseObject(person, Person.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;

    }

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
        logger.info("Finding all people");
        
        var personPage = repository.findAll(pageable);
        var personVosPage = personPage.map( p -> DozerMapper.parseObject(p, PersonVO.class));
        
        personVosPage.map(p -> p.add(
                linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        
        Link link = linkTo(methodOn(PersonController.class).findAll(
                    pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        
        return assembler.toModel(personVosPage, link);
    }

    public PagedModel<EntityModel<PersonVO>> findPersonByName(String firstName, Pageable pageable) {
        logger.log(Level.INFO, "Finding people with name like: {0}", firstName);
        
        var personPage = repository.findPersonByName(firstName ,pageable);
        var personVosPage = personPage.map( p -> DozerMapper.parseObject(p, PersonVO.class));
        
        personVosPage.map(p -> p.add(
                linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        
        Link link = linkTo(methodOn(PersonController.class).findAll(
                    pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        
        return assembler.toModel(personVosPage, link);
    }

    public PersonVO findById(Long id) {
        logger.log(Level.INFO, "Finding person of id:{0}", id.toString());

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        var vo = DozerMapper.parseObject(entity, PersonVO.class);

        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person) {
        if (person == null) {
            throw new RequiredObjectIsNullException();
        }

        Person entity = repository.findById(person.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Key"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    @Transactional
    public PersonVO disablePerson(Long id) {
        logger.log(Level.INFO, "Disabling person of id:{0}", id.toString());

        repository.disablePerson(id);

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        var vo = DozerMapper.parseObject(entity, PersonVO.class);

        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.log(Level.INFO, "Deleting person of id:{0}", id.toString());
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        repository.delete(entity);
    }

    public PersonVOV2 createV2(PersonVOV2 person) {
        logger.info("Creating person with V2");
        var entity = mapper.convertVOToEntity(person);
        var vo = mapper.convertEntityToVO(repository.save(entity));
        return vo;
    }

}
