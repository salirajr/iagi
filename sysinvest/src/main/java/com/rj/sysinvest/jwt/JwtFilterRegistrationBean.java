package com.rj.sysinvest.jwt;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.Filter;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Component
@Data
public class JwtFilterRegistrationBean extends FilterRegistrationBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilterRegistrationBean.class);
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
