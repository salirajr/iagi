package com.rj.sysinvest.security.jwt;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.Filter;
import lombok.Data;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Component
@Data
public class JwtFilterRegistrationBean extends FilterRegistrationBean {

    @Resource
    private JwtFilter jwtFilter;
    /* 
    the value is in csv format i.e. : /api/*,/private/*,/rahasia/*
    so it can be configured in properties file or text file
     */
    private String jwtUrlPatterns = "/api/*";
    private String jwtUrlPatternsDelimiter = ",";

    @Override
    public Filter getFilter() {
        return jwtFilter;
    }

    @PostConstruct
    void parseJwtUrlCsv() {
        String[] urls = jwtUrlPatterns.split(jwtUrlPatternsDelimiter);
        for (String urlPattern : urls) {
            addUrlPatterns(urlPattern);
        }
    }

}
