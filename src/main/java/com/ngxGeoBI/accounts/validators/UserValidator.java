/**
 * Created by : Sukesh Laghate
 * Created on : 28-12-2017
 **/
package com.ngxGeoBI.accounts.validators;

import com.ngxGeoBI.accounts.Helper;
import com.ngxGeoBI.accounts.model.User;
import com.ngxGeoBI.accounts.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator  implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return  userService.equals(aClass);
    }

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Override
    public void validate(Object o, Errors errors) {
        logger.info("inside validator");

        User user = (User) o;

        logger.info("user object " , user.getUsername());

        // username can not contain whitespace or can not be empty
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"username","NotEmpty");

        if(user.getUsername().length() < Helper.MIN_LENGTH_USERNAME  ||
                user.getUsername().length()>Helper.MAX_LENGTH_USERNAME){
            errors.rejectValue("Username", "Size.userForm.username");
        }

        // A user with this  username exists
        if(userService.findByUsername(user.getUsername()).isPresent()){
            errors.rejectValue("Username", "Duplicate.userForm.username");
        }

        // Password validation password can not be empty or contain white space
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");

        if (user.getPassword().length() < Helper.MIN_LENGTH_PASSWORD  ||
                user.getPassword().length() > Helper.MAX_LENGTH_PASSWORD) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}
