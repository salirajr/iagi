package com.rj.sysinvest.jwt;

import io.jsonwebtoken.JwtException;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtFilter extends GenericFilterBean {

    @Resource
    private JwtService jwtService;

    public static final String AUTHORIZATION = "Authorization", BEARER = "Bearer ",
            CLAIMS = "claims";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;

        String authHeader = httpReq.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            throw new ServletException("Missing or invalid Authorization header.");
        }

        try {
            String jwt = authHeader.substring(BEARER.length());
            httpReq.setAttribute(CLAIMS, jwtService.parseJwt(jwt));
        } catch (JwtException e) {
            throw new ServletException("Invalid token.", e);
        }

        chain.doFilter(req, res);
    }

}
