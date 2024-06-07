/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lrz.apigateway.repository;

import com.lrz.apigateway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author lara
 */
public interface UserRepository extends JpaRepository<User, Long>{
    
    @Query("SELECT u FROM User WHERE u.userName =:userName ")
    User findByUsername(@Param("userName")String username);
}
