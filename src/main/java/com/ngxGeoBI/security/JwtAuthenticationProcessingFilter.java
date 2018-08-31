/**
 * Created by : Sukesh Laghate
 * Created on : 11-01-2018
 **/
package com.ngxGeoBI.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWT;
import com.ngxGeoBI.security.config.JwtSettings;
import com.ngxGeoBI.security.exceptions.InvalidTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class JwtAuthenticationProcessingFilter extends
        AbstractAuthenticationProcessingFilter{
    private final AuthenticationFailureHandler failureHandler;
    private final JwtTokenService jwtTokenService;

    public JwtAuthenticationProcessingFilter(AuthenticationFailureHandler failureHandler,
                                             JwtTokenService jwtTokenService, RequestMatcher requestMatcher) {
        super(requestMatcher);
        this.failureHandler = failureHandler;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException,
            IOException, ServletException {
        String tokenPayload = request.getHeader(JwtSettings.JWT_TOKEN_HEADER_NAME);
        // Since we are using Nimbus JOSE-JWT we need to use the compacted string and then convert
        // the string to verified token.
        String compactToken = jwtTokenService.extract(tokenPayload);
        JWT token = null;
        try {
            jwtTokenService.verify(compactToken);
            token = jwtTokenService.getTokenFromCompact(compactToken);
        } catch (ParseException e) {
            throw new InvalidTokenException("Could not parse token");
        } catch (JOSEException e) {
            throw new InvalidTokenException("Could not verify token");
        }

        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                         Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
