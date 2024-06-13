package com.lrz.apigateway.services;

import com.lrz.apigateway.data.vo.v1.security.AccountCredentialsVO;
import com.lrz.apigateway.security.jwt.JwtTokenProvider;
import com.lrz.apigateway.repository.UserRepository;
import com.lrz.apigateway.data.vo.v1.security.TokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServices {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    @SuppressWarnings("rawtypes")
    public ResponseEntity<TokenVO> signin(AccountCredentialsVO data) {
        try {
            var username = data.getUsername();
            var password = data.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            var user = repository.findByUsername(username);

            if (user != null) {
                var token = tokenProvider.createAccessToken(username, user.getRoles());
                var refreshToken = tokenProvider.getRefreshToken(username, user.getRoles(), new Date());
               
                TokenVO tokenVO = new TokenVO();
                tokenVO.setUsername(username);
                tokenVO.setAuthenticated(true);
                tokenVO.setCreated(new Date());
                tokenVO.setExpiration(token.getExpiration());
                tokenVO.setAccessToken(token.getAccessToken());
                tokenVO.setRefreshToken(refreshToken);

                return ResponseEntity.ok(tokenVO);
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }
    
    public ResponseEntity refreshToken(String username, String refreshToken){
        var user = repository.findByUsername(username);
        var tokenResponse = new TokenVO();
        
        if(user != null){
            
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        }else {
            throw new UsernameNotFoundException("Username: " +username + " not found");
        }
        
        return ResponseEntity.ok(tokenResponse);
    }
}
