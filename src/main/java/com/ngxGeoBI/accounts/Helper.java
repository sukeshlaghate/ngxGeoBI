package com.ngxGeoBI.accounts;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Helper {
    public static final int MIN_LENGTH_USERNAME = 3;
    public static final int MAX_LENGTH_USERNAME = 32;

    public static final int MIN_LENGTH_PASSWORD = 8;
    public static final int MAX_LENGTH_PASSWORD = 32;

    public static final int MIN_LENGTH_FIRST_NAME = 2;
    public static final int MAX_LENGTH_FIRST_NAME = 32;

    public static final int MIN_LENGTH_LAST_NAME = 2;
    public static final int MAX_LENGTH_LAST_NAME = 32;

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
}
