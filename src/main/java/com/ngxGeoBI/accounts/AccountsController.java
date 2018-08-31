/**
 * Created by : Sukesh Laghate
 * Created on : 28-12-2017
 **/
package com.ngxGeoBI.accounts;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import com.ngxGeoBI.accounts.model.User;
//import com.ngxGeoBI.accounts.services.SecurityService;
import com.ngxGeoBI.accounts.services.UserService;
import com.ngxGeoBI.accounts.validators.UserValidator;
import com.ngxGeoBI.common.ErrorCode;
import com.ngxGeoBI.common.ErrorResponse;
import com.ngxGeoBI.security.JwtTokenService;
import com.ngxGeoBI.security.ajax.dto.UserContext;
import com.ngxGeoBI.security.config.JwtSettings;
import com.ngxGeoBI.security.exceptions.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class AccountsController {
    @Autowired private UserService userService;

    // Currently we do not want users to autlogin once they are registered.
//    @Autowired
//    private SecurityService securityService;

    @Autowired  private UserValidator userValidator;

    @Autowired  private JwtTokenService tokenTokenService;

    @Autowired  private JwtTokenService jwtTokenService;

    // @Autowired private UserProfileService userProfileService;

    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

    @RequestMapping(value = "auth/register", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody    String  registration(@RequestBody User userForm, BindingResult bindingResult) {

        logger.info("Registration method post  :" , userForm.getUsername());
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {

            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            message.add("User Registration Error :");
            for (FieldError e : errors){
                message.add("@" + e.getField().toUpperCase() + ":" + e.getRejectedValue());
            }
            ErrorResponse error = ErrorResponse.of(message.toString() , ErrorCode.REGISTRATION, HttpStatus.EXPECTATION_FAILED );
            return error.toString();
        }
        //User user = new User(userForm);
        userService.save(userForm);

        // securityService.autologin(userForm.getUsername(), userForm.getPassword());

        return "redirect:/";
    }

    @RequestMapping(value = "auth/register", method = RequestMethod.GET,  produces = "application/json")
    public @ResponseBody String  registration() {

        return "you can  use post method on same end point with user details to register the user ";
    }


    @RequestMapping(value = "auth/logout", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        session.invalidate();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return " you have been logged out";
    }


    @RequestMapping(value="/auth/token", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody String refreshToken(javax.servlet.http.HttpServletRequest request,
                                             HttpServletResponse response) throws IOException, ServletException {

        String tokenPayload = request.getHeader(JwtSettings.JWT_TOKEN_HEADER_NAME);
        // Since we are using Nimbus JOSE-JWT we need to use the compacted string and then convert
        // the string to verified token.
        String compactToken = tokenTokenService.extract(tokenPayload);
        JWT token = null;
        String refreshResponse = " ";
        try {
            tokenTokenService.verify(compactToken);
            token = tokenTokenService.getTokenFromCompact(compactToken);
        } catch (ParseException e) {
            throw new InvalidTokenException("Could not parse token");
        } catch (JOSEException e) {
            throw new InvalidTokenException("Could not verify token");
        }

        // Check the JWT type
        if (token instanceof PlainJWT) {
            PlainJWT plainObject = (PlainJWT)token;
            // continue processing of plain JWT...
        } else if (token instanceof SignedJWT) {
            SignedJWT jwsObject = (SignedJWT)token;
            // continue with signature verification...
            try {
                String subject = jwsObject.getJWTClaimsSet().getClaim("sub").toString();
                User user = userService.findByUsername(subject)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));
                if (user.getRoles() == null) throw new InsufficientAuthenticationException("User has no roles assigned");
                List<GrantedAuthority> authorities = user.getRoles().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getRole()))
                        .collect(Collectors.toList());

                UserContext userContext = UserContext.create(user.getUsername(), user.getId(), authorities);
                refreshResponse = jwtTokenService.createAccessJwtToken(userContext);

            } catch (ParseException e) {
                throw new InvalidTokenException("Could not parse claims");
            } catch (JOSEException e) {
                e.printStackTrace();
            }
        } else if (token instanceof EncryptedJWT) {
            EncryptedJWT jweObject = (EncryptedJWT)token;
            // continue with decryption...
        }
        return refreshResponse;
    }
}
