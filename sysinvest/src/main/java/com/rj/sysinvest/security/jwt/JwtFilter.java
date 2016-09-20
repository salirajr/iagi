package com.rj.sysinvest.security.jwt;

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
import java.util.Map;
import com.rj.sysinvest.security.login.RoleUriAuthenticationService;
import java.util.List;

@Component
@Data
public class JwtFilter extends GenericFilterBean {

    @Resource
    private JwtService jwtService;
    @Resource
    private RoleUriAuthenticationService roleAccessService;

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

        Map<String, Object> claims;
        try {
            String jwt = authHeader.substring(BEARER.length());
            claims = jwtService.parseJwt(jwt);
            httpReq.setAttribute(CLAIMS, claims);
        } catch (JwtException e) {
            throw new ServletException("Invalid token.", e);
        }

        String uri = httpReq.getRequestURI();
        List<String> roles = (List<String>) claims.get("roles");
        for (String role : roles) {
            boolean hasAccess = roleAccessService.hasAccess(role, uri);
            if (!hasAccess) {
                throw new ServletException("User has no role access to URI " + uri);
            }
        }

        chain.doFilter(req, res);
    }

}
