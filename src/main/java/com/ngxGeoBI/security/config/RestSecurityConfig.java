/**
 * Created by : Sukesh Laghate
 * Created on : 12-01-2018
 **/
package com.ngxGeoBI.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngxGeoBI.common.SkipPathRequestMatcher;
import com.ngxGeoBI.ngxGeoBI.ngxGeoBiCorsFilter;
import com.ngxGeoBI.security.JwtAuthenticationProcessingFilter;
import com.ngxGeoBI.security.JwtAuthenticationProvider;
import com.ngxGeoBI.security.JwtTokenService;
import com.ngxGeoBI.security.RestAuthenticationControlPoint;
import com.ngxGeoBI.security.ajax.AjaxAuthenticationProvider;
import com.ngxGeoBI.security.ajax.AjaxLoginProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
class RestSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String LANDING_PAGE_URL = "/landing";
    public static final String AUTHENTICATION_URL = "/api/auth/login";
    public static final String REFRESH_TOKEN_URL = "/api/auth/token";
    public static final String REGISTRATION_URL = "/api/auth/register";
    public static final String LOGOUT_URL = "/api/auth/logout";
    public static final String API_ROOT_URL = "/api/**";

    @Autowired
    private RestAuthenticationControlPoint authenticationEntryPoint;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private AjaxAuthenticationProvider ajaxAuthenticationProvider;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter(String loginEntryPoint)
            throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(loginEntryPoint,
                successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);

        return filter;
    }

    protected JwtAuthenticationProcessingFilter buildJwtAuthenticationProcessingFilter(List<String> pathsToSkip,
                                                                                       String pattern)
            throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        JwtAuthenticationProcessingFilter filter =
                new JwtAuthenticationProcessingFilter(failureHandler, jwtTokenService, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(
                LANDING_PAGE_URL,
                AUTHENTICATION_URL,
                LOGOUT_URL,
                REGISTRATION_URL,
                REFRESH_TOKEN_URL
        );

        http
                .csrf().disable() // We don't need CSRF for JWT based authentication
                .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPoint)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
                .permitAll()

                .and()
                .authorizeRequests()
                .antMatchers(API_ROOT_URL).authenticated() // Protected API End-points

                .and()
                .addFilterBefore(new ngxGeoBiCorsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildAjaxLoginProcessingFilter(AUTHENTICATION_URL), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtAuthenticationProcessingFilter(permitAllEndpointList,
                        API_ROOT_URL), UsernamePasswordAuthenticationFilter.class);

    }
}
