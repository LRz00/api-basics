package com.lrz.apigateway.services;

import com.lrz.apigateway.controller.PersonController;
import com.lrz.apigateway.data.vo.v1.PersonVO;
import com.lrz.apigateway.data.vo.v2.PersonVOV2;
import java.util.List;
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
import java.util.logging.Level;

@Service
public class PersonServices {

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper mapper;

    Logger logger = Logger.getLogger(PersonServices.class.getName());

    public PersonVO create(PersonVO person) {
        logger.info("Creating person");
        var entity = DozerMapper.parseObject(person, Person.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;

    }

    public List<PersonVO> findAll() {
        logger.info("Finding all persons");
        var persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);

        persons.stream()
                .forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        return persons;
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
