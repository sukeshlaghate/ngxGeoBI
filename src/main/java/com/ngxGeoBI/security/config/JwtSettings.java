/**
 * Created by : Sukesh Laghate
 * Created on : 09-01-2018
 **/
package com.ngxGeoBI.security.config;


public class JwtSettings {
    public static final String TOKEN_ISSUER = "ngxGeoBI";
    public static final int EXPIERS_IN_MIN = 30;
    public static final int REFRESH_EXPIRES_IN_MIN = 60;
    public static final String SECRET_KEY = "oy-g2sfy0oxpw8oe_vzlav*aqipw%&f)a&d1)$f2w*p&6)2367";
    public static final String HEADER_PREFIX = "Bearer";
    public static final String REFRESH_TOKEN_SCOPE_ROLE="ROLE_REFRESH_TOKEN";
    public static final String JWT_TOKEN_HEADER_NAME = "X-Authorization";

}
