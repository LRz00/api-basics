package com.lrz.apigateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lrz.apigateway.model.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
    
    @Modifying
    @Query("UPDATE Person p  SET p.enabled = false WHERE p.id =:id")
    void disablePerson(@Param("id") Long id);
}