package com.lrz.apigateway.security.jwt;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1 hour

    @Autowired
    private UserDetailsService userDetailsService;

    private Algorithm algorithm;

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenVO createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date valid = new Date(now.getTime() + validityInMilliseconds);
        String accessToken = getAccessToken(username, roles, now, valid);
        String refreshToken = getRefreshToken(username, roles, now);

        return new TokenVO(username, true, now, valid, accessToken, refreshToken);
    }

    private String getAccessToken(String username, List<String> roles, Date now, Date valid) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(valid)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm);
    }

    public String getRefreshToken(String username, List<String> roles, Date now) {
        Date validRefreshToken = new Date(now.getTime() + validityInMilliseconds * 3); // 3 hours

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validRefreshToken)
                .withSubject(username)
                .sign(algorithm);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJwt = decodedToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJwt.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJwt = decodedToken(token);
            if (decodedJwt.getExpiresAt().before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
        }
    }
}

