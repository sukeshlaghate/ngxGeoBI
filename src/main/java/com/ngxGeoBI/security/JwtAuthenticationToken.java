/**
 * Created by : Sukesh Laghate
 * Created on : 08-01-2018
 **/
package com.ngxGeoBI.security;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final JWT token;
    private final Collection<GrantedAuthority> authorities;
    private boolean authenticated;
    private JWTClaimsSet claims;


    public JwtAuthenticationToken(JWT  token) {
        super(null);
        this.token = token;
        List<String> roles;
        try {
            roles = token.getJWTClaimsSet().getStringListClaim("roles");
        } catch (ParseException e) {
            roles = new ArrayList<>();
        }
        List<GrantedAuthority> tmp = new ArrayList<>();
        if (roles != null) {
            for (String role : roles) {
                tmp.add(new SimpleGrantedAuthority(role));
            }
        }
        this.authorities = Collections.unmodifiableList(tmp);
        try {
            this.claims = token.getJWTClaimsSet();
        } catch (ParseException e) {
           this.claims = null;
        }
        authenticated = false;
    }

    public JWT getToken()
    {
        return this.token;
    }

    public JWTClaimsSet getClaims() {
        return claims;
    }

    @Override
    // ToDo: implement  code to get authority by parsing the signature and getting the user
    public Collection<GrantedAuthority> getAuthorities(){
        return this.authorities;
    }

    @Override
    public Object getCredentials(){
        return token;
    }

    @Override
    public Object getDetails() {
        return claims.toJSONObject();
    }

    @Override
    public Object getPrincipal() {
        return claims.getSubject() ;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            this.authenticated = isAuthenticated;
    }


    @Override
    public String getName() {
        return claims.getSubject();
    }
}
