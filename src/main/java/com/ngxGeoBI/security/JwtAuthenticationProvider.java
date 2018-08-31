/**
 * Created by : Sukesh Laghate
 * Created on : 08-01-2018
 **/
package com.ngxGeoBI.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.*;
import com.ngxGeoBI.security.exceptions.EncryptedTokenException;
import com.ngxGeoBI.security.exceptions.InvalidSignatureException;
import com.ngxGeoBI.security.exceptions.InvalidTokenException;
import com.ngxGeoBI.security.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.ngxGeoBI.security.config.JwtSettings.TOKEN_ISSUER;

@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider  implements AuthenticationProvider {

    // JWT TokenService is a utility service responsible for generating, refreshing and verifying tokens
    // and other JWT related utilities
    @Autowired
    private JwtTokenService jwtService;

    @Override
    @Transactional
    public Authentication authenticate( Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        JWT jwToken = jwtAuthenticationToken.getToken();

        // Check type of the parsed JOSE Object
        if( jwToken instanceof PlainJWT){
            handlePlainToken((PlainJWT) jwToken);

        } else  if(  jwToken instanceof  SignedJWT ) {
            handleSignedToken( (SignedJWT) jwToken);
        } else if(  jwToken instanceof  EncryptedJWT ) {
            handleEncryptedToken( (EncryptedJWT)  jwToken) ;
        }
        //Date referenceTime = new Date();
        LocalDateTime referenceTime = LocalDateTime.now();
        JWTClaimsSet claims = jwtAuthenticationToken.getClaims();

        //Date expirationTime = claims.getExpirationTime();
        LocalDateTime expirationTime = LocalDateTime.ofInstant(claims.getExpirationTime().toInstant(), ZoneId.systemDefault());
        //if( expirationTime == null || expirationTime.before(referenceTime))
        if( expirationTime == null || expirationTime.isBefore(referenceTime))
        {
            throw new TokenExpiredException("Provided token has expired");
        }

        //Date notBeforeTime = claims.getNotBeforeTime();
//        LocalDateTime notBeforeTime = LocalDateTime.ofInstant(claims.getNotBeforeTime().toInstant(), ZoneId.systemDefault());
//        if( notBeforeTime == null || notBeforeTime.isBefore(referenceTime)){
//            throw new InvalidTokenException("Not before is after sysdate");
//        }

        String issuer = claims.getIssuer();
        if( !TOKEN_ISSUER .equals(issuer)){
            throw new InvalidTokenException("Invalid Issuing authority");
        }

        //everycheck has passed now we can set user as authenticated user
        jwtAuthenticationToken.setAuthenticated(true);
        return jwtAuthenticationToken;
    }

    @Override
    public  boolean supports (Class<?> authentication){

        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private void handlePlainToken(PlainJWT jwToken){
        throw new InvalidTokenException("Unsecured plain tokens are not supported");
    }
    
    private void handleSignedToken( SignedJWT jwToken){
        try{
            if( !jwToken.verify(jwtService.getVerifier( ) ) ) {
                throw new InvalidSignatureException("Invalid Signature for JWT");
            }
        } catch (JOSEException e) {
            throw new InvalidSignatureException("Invalid Signature for JWT");
        }
    }

    private void handleEncryptedToken( EncryptedJWT  jwToken) {
        throw new EncryptedTokenException("Unsupported Token Type");
    }

}
