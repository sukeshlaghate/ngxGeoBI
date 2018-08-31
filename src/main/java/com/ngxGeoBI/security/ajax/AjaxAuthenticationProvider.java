/**
 * Created by : Sukesh Laghate
 * Created on : 11-01-2018
 **/
package com.ngxGeoBI.security.ajax;

import com.ngxGeoBI.accounts.model.User;
import com.ngxGeoBI.accounts.services.UserService;
import com.ngxGeoBI.security.ajax.dto.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("unchecked")
public class AjaxAuthenticationProvider implements AuthenticationProvider {

    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AjaxAuthenticationProvider.class);
    @Autowired
    public AjaxAuthenticationProvider(BCryptPasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        if(authentication == null)
            throw new BadCredentialsException("No authentication data provided");
        logger.info("------------------ Authentication method start -----------------");
        String username;
        String password;
        Object principal = authentication.getPrincipal();

        if( principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            password = ((UserDetails) principal).getPassword();
            logger.info(MessageFormat.format("===========> UserDetails  <============== \n {0}",username));
        } else if ( principal instanceof UserContext){
            username = ((UserContext) principal).getUsername();
            password = (String) authentication.getCredentials();
            logger.info(MessageFormat.format("===========> UserContext  <============== \n {0}",username));
        } else {
            username = (String) authentication.getPrincipal();
            password = (String) authentication.getCredentials();
            logger.info(MessageFormat.format("===========> else part  <============== \n {0}",username));
        }

        logger.info(MessageFormat.format(" Searching user {0} ", username));
        Optional <User> userOptional = userService.findByUsername(username);
        userOptional.orElseThrow(() -> new BadCredentialsException("Bad Credentials supplied"));
        User user = userOptional.get();
        logger.info(MessageFormat.format("is user present {0}", userOptional.isPresent()));
        logger.info(MessageFormat.format(" verifying password for user {0} ", userOptional.get().getUsername()));
        if( !encoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("Authentication failed. Please supply correct credentials");
        }
        logger.info(MessageFormat.format("Getting Roles for user {0} ", userOptional.get().getUsername()));
        if( user.getRoles().isEmpty() )
            throw new InsufficientAuthenticationException("User has no roles assigned");

        logger.info(MessageFormat.format("Getting authorities for user {0} ", username));
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(authority-> new SimpleGrantedAuthority(authority.getRole()))
                .collect(Collectors.toList());
        logger.info(MessageFormat.format("creating User Context for user {0} ", username));
        UserContext userContext = UserContext.create(user.getUsername(), user.getId(), authorities);
        logger.info("------------------ Authentication method end -----------------");
        // Since we are satisfied with the credentials provided, we are going to produce a trusted
        // user and remove the password from the basic token.
        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authenticationClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authenticationClass);
    }
}
