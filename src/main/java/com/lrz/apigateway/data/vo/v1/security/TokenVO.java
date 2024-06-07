/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.data.vo.v1.security;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author lara
 */
public class TokenVO implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String username;
    private Boolean authenticated;
    private Date created;
    private Date expiration;
    private String accessToken;
    private String refreshToken;

    public TokenVO(String username, Boolean authenticated, Date created, Date expiration, String accessToken, String refreshToken) {
        this.username = username;
        this.authenticated = authenticated;
        this.created = created;
        this.expiration = expiration;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public TokenVO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.username);
        hash = 37 * hash + Objects.hashCode(this.authenticated);
        hash = 37 * hash + Objects.hashCode(this.created);
        hash = 37 * hash + Objects.hashCode(this.expiration);
        hash = 37 * hash + Objects.hashCode(this.accessToken);
        hash = 37 * hash + Objects.hashCode(this.refreshToken);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TokenVO other = (TokenVO) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.accessToken, other.accessToken)) {
            return false;
        }
        if (!Objects.equals(this.refreshToken, other.refreshToken)) {
            return false;
        }
        if (!Objects.equals(this.authenticated, other.authenticated)) {
            return false;
        }
        if (!Objects.equals(this.created, other.created)) {
            return false;
        }
        return Objects.equals(this.expiration, other.expiration);
    }

    
}
