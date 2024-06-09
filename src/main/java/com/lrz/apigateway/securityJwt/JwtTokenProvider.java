/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.securityJwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lrz.apigateway.data.vo.v1.security.TokenVO;
import com.lrz.apigateway.exception.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author lara
 */
@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";
    @Value("${security.jwt.token.expire-lenght:3600000}")
    private long validityInMiliseconds = 3600000; //1 hora

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithim = null;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithim = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenVO createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date valid = new Date(now.getTime() + validityInMiliseconds);
        var accessToken = getAcessToken(username, roles, now, valid);
        var refreshToken = getRefreshToken(username, roles, now);

        return new TokenVO(username, true, now, valid, accessToken, refreshToken);
    }

    private String getAcessToken(String username, List<String> roles, Date now, Date valid) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        
       return  JWT.create().withClaim("roles", roles).
                withIssuedAt(now)
                .withExpiresAt(valid)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithim)
                .strip();

    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        Date validRefreshToken = new Date(now.getTime() + validityInMiliseconds * 3); //3 horas
        
        return  JWT.create().withClaim("roles", roles).
                withIssuedAt(now)
                .withExpiresAt(validRefreshToken)
                .withSubject(username)
                .sign(algorithim)
                .strip();
    }
    
    public Authentication getAuthentication(String token){
        DecodedJWT decodedJwt = decodedToken(token);
        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJwt.getSubject());
        
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decodedJwt = verifier.verify(token);
       
        return decodedJwt;
    }
    
    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }
    
    public boolean validateToken(String token){
        DecodedJWT  decodedJwt = decodedToken(token);
        
        try {
            if (decodedJwt.getExpiresAt().before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
        }
    }
}
