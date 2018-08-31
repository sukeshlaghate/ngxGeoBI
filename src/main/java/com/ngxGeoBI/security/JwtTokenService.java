/**
 * Created by : Sukesh Laghate
 * Created on : 08-01-2018
 **/
package com.ngxGeoBI.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.*;
import com.ngxGeoBI.security.ajax.dto.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;


import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import static com.ngxGeoBI.security.config.JwtSettings.*;

@Component
public class JwtTokenService  {

    private  JWSVerifier jwsVerifier;
    private  JWSSigner jwsSigner;

    public JwtTokenService(){
        try {
            this.jwsVerifier = new MACVerifier(SECRET_KEY);
            this.jwsSigner = new MACSigner(SECRET_KEY);
        } catch (JOSEException e) {
            e.printStackTrace();
        }
    }

    public final JWSVerifier   getVerifier(){
        return this.jwsVerifier;
    }

    public String extract(String header) {
        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }

        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }

        return header.substring(HEADER_PREFIX.length(), header.length());
    }

    public String createAccessJwtToken(UserContext userContext) throws JOSEException {
        if(StringUtils.isBlank(userContext.getUsername()))
            throw new IllegalArgumentException("Cannot create JWT token without user");

        if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty())
            throw new IllegalArgumentException("User doesn't have any privileges");

        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime expiry = currentTime.plusMinutes(EXPIERS_IN_MIN);
        Date expiryDate = Date.from(expiry.atZone(ZoneId.systemDefault()).toInstant());

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userContext.getUsername())
                .issuer(TOKEN_ISSUER)
                .expirationTime(expiryDate)
                .claim("roles", userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()))
                .claim("id", userContext.getUserid())
                .build();

        // Prepare JWT with claims set
//        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
//                .subject(userContext.getUsername())
//                .issuer(TOKEN_ISSUER)
//                .expirationTime(new Date(new Date().getTime() + EXPIERS_IN_MIN * 1000))
//                .claim("roles", userContext.getAuthorities())
//                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        // Apply the HMAC protection
        signedJWT.sign(this.jwsSigner);


        // Serialize to compact form, produces something like
        // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
        return signedJWT.serialize();
    }

    public String createRefreshToken(UserContext userContext) throws JOSEException {
        if(StringUtils.isBlank(userContext.getUsername()))
            throw new IllegalArgumentException("Cannot create JWT token without user");

        // long delta = REFRESH_EXPIRES_IN_MIN * 1000l;
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime expiry = currentTime.plusMinutes(REFRESH_EXPIRES_IN_MIN);
        Date expiryDate = Date.from(expiry.atZone(ZoneId.systemDefault()).toInstant());

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userContext.getUsername())
                .issuer(TOKEN_ISSUER)
                .expirationTime(expiryDate)
                .claim("scopes", Arrays.asList(REFRESH_TOKEN_SCOPE_ROLE))
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        // Apply the HMAC protection
        signedJWT.sign(jwsSigner);
        // Serialize to compact form, produces something like
        // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
        return signedJWT.serialize();
    }

    public boolean verify(String compactToken) throws ParseException, JOSEException {

        JWT jwt = JWTParser.parse(compactToken);
        boolean verified = false;
        // Check the JWT type
        if (jwt instanceof PlainJWT) {
            PlainJWT plainObject = (PlainJWT)jwt;
            //todo find mechanism to verify plain jwt
        } else if (jwt instanceof SignedJWT) {
            SignedJWT jwsObject = (SignedJWT)jwt;
            verified = jwsObject.verify(this.jwsVerifier);
        } else if (jwt instanceof EncryptedJWT) {
            EncryptedJWT jweObject = (EncryptedJWT)jwt;
            //todo find mechanism to verify encrypted jwt
        }
        return verified;
    }

    public JWT getTokenFromCompact(String compactToken) throws ParseException {
        JWT token = JWTParser.parse(compactToken);
        return token;
    }


}
