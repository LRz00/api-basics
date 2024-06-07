/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.services;

import com.lrz.apigateway.mapper.custom.PersonMapper;
import com.lrz.apigateway.repository.UserRepository;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author lara
 */
@Service
public class UserServices implements UserDetailsService{
     @Autowired
    UserRepository repository;

    @Autowired
    PersonMapper mapper;

    Logger logger = Logger.getLogger(UserServices.class.getName());
    
    
    public UserServices(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding user by username:" + username);
        
        var user = repository.findByUsername(username);
        
        if(user!= null){
            return user;
        }else{
            throw new UsernameNotFoundException("Username: " + username + "Not found.");
        }
    }

    
}
