package com.rj.sysinvest.jwt;

import io.jsonwebtoken.JwtException;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

import java.util.Map;

public class JwtFilter extends GenericFilterBean {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;

        String authHeader = httpReq.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid Authorization header.");
        }

        try {
            Map<String, Object> claims = jwtService.parseJwt(authHeader);
            httpReq.setAttribute("claims", claims);
        } catch (JwtException e) {
            throw new ServletException("Invalid token.", e);
        }

        chain.doFilter(req, res);
    }

}
