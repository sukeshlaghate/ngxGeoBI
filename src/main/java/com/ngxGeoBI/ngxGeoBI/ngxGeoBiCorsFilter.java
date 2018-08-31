/**
 * Created by : Sukesh Laghate
 * Created on : 10-01-2018
 **/
package com.ngxGeoBI.ngxGeoBI;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

public class ngxGeoBiCorsFilter extends CorsFilter {
    public ngxGeoBiCorsFilter() {
        super(configurationSource());
    }

    // ToDO: it is dangerous to allow all hosts restrict hosts in  allowed Origin
    private static UrlBasedCorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.setMaxAge(36000L);
        config.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
