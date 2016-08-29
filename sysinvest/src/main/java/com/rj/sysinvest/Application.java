package com.rj.sysinvest;

import com.rj.sysinvest.jwt.JwtFilter;
import com.rj.sysinvest.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import org.springframework.context.annotation.Bean;

/**
 *
 * @author rais
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Autowired
    public FilterRegistrationBean jwtFilter(JwtService jwtService) {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new JwtFilter(jwtService));
        bean.addUrlPatterns("/api/*");
        return bean;
    }

}
