/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.mapper.custom;

import com.lrz.apigateway.model.Person;
import org.springframework.stereotype.Service;
import com.lrz.apigateway.data.vo.v2.PersonVOV2;
import java.util.Date;
/**
 *
 * @author lara
 */

@Service
public class PersonMapper {
    public PersonVOV2 convertEntityToVO(Person person){
        PersonVOV2 vo = new PersonVOV2();
        
        vo.setId(person.getId());
        vo.setAddress(person.getAddress());
        vo.setBirthDay(new Date());
        vo.setFirstName(person.getFirstName());
        vo.setLastName(person.getLastName());
        vo.setGender(person.getGender());
        
        return vo;
    } 
    
     public Person convertVOToEntity(PersonVOV2 person){
        Person entity = new Person();
        
        entity.setId(person.getId());
        entity.setAddress(person.getAddress());
        //entity.setBirthDay(new Date());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setGender(person.getGender());
        
        return entity;
    }
}
