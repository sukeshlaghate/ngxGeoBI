package com.ngxGeoBI.ngxGeoBI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ngxGeoBI.accounts*",
		"com.ngxGeoBI.ngxGeoBI*",
		"com.ngxGeoBI.common*",
		"com.ngxGeoBI.security*"})
@EntityScan("com.ngxGeoBI*")
@EnableJpaRepositories("com.ngxGeoBI*")
public class NgxGeoBiApplication /* extends SpringBootServletInitializer */ {
// uncomment this and extends line when deploying as war application
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(NgxGeoBiApplication.class);
//	}
	public static void main(String[] args) {
		SpringApplication.run(NgxGeoBiApplication.class, args);
	}
}
