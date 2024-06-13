/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.controller;

import com.lrz.apigateway.data.vo.v1.security.AccountCredentialsVO;
import com.lrz.apigateway.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lara
 */
@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthServices authServices;

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Authenticates a user and returns a token")
    @PostMapping(value = "/signin")
    public ResponseEntity sigin(@RequestBody AccountCredentialsVO data) {
        if (IsParamsNotNull(data)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }
        var token = authServices.signin(data);
        if(token == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }
        return token;
        
    }
    @SuppressWarnings("rawtypes")
    @Operation(summary = "Refreshs token for auhtenticated user and returns a token")
    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if (isParamsNotNull(refreshToken, username) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }
        var token = authServices.refreshToken(username, refreshToken);
        if(token == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }
        return token;
        
    }

    private static boolean isParamsNotNull(String refreshToken, String username) {
        return refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank();
    }

    private static boolean IsParamsNotNull(AccountCredentialsVO data) {
        return data == null || data.getUsername() == null || data.getUsername().isBlank()
                || data.getPassword() == null || data.getPassword().isBlank();
    }
}
