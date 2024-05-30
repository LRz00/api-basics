package com.lrz.apigateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lrz.apigateway.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{

}