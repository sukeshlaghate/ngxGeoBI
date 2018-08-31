/**
 * Created by : Sukesh Laghate
 * Created on : 11-01-2018
 **/
package com.ngxGeoBI.security.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.ngxGeoBI.security.JwtTokenService;
import com.ngxGeoBI.security.ajax.dto.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final JwtTokenService jwtTokenService;
    private static final Logger logger = LoggerFactory.getLogger(AjaxAuthenticationSuccessHandler.class);

    @Autowired
    public AjaxAuthenticationSuccessHandler(ObjectMapper objectMapper, JwtTokenService jwtTokenService) {
        this.objectMapper = objectMapper;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        UserContext userContext = (UserContext) authentication.getPrincipal();
        logger.info(MessageFormat.format("in onAuthenticationSuccess {0}", userContext.getUsername()));
        String authToken ="";
        try {
            authToken = jwtTokenService.createAccessJwtToken(userContext);
        } catch (JOSEException e) {
            logger.debug("AccessToken Exception",e.getMessage());
        }
        String refreshToken = null;
        try {
            refreshToken = jwtTokenService.createRefreshToken(userContext);
        } catch (JOSEException e) {
            logger.debug("AccessToken Exception",e.getMessage());
        }

        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", authToken);
        tokenMap.put("refreshToken", refreshToken);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), tokenMap);

        clearAuthenticationAttributes(httpServletRequest);

    }

    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     */
    protected void clearAuthenticationAttributes(HttpServletRequest request){

        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}
