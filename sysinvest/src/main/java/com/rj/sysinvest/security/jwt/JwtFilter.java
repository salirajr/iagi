package com.rj.sysinvest.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.sysinvest.security.exception.AppSecurityException;
import com.rj.sysinvest.security.exception.AppSecurityExceptionBean;
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
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.rj.sysinvest.security.login.SecurityRoleService;

@Component
@Data
public class JwtFilter extends GenericFilterBean {

    @Resource
    private SecurityJwtComponent jwtComp;
    @Resource
    private SecurityRoleService roleService;
    @Resource
    private ObjectMapper mapper;

    public static final String AUTHORIZATION = "Authorization", BEARER = "Bearer ",
            CLAIMS = "claims";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;

        try {
            String authHeader = httpReq.getHeader(AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith(BEARER)) {
                throw new AppSecurityException(500, "Missing or invalid Authorization header.");
            }

            SecurityClaims claims;
            try {
                String jwt = authHeader.substring(BEARER.length());
                claims = jwtComp.parseJwt(jwt);
                httpReq.setAttribute(CLAIMS, claims);
            } catch (JwtException e) {
                throw new AppSecurityException(500, "Invalid token.", e);
            }

            String uri = httpReq.getRequestURI();
            List<String> roles = (List<String>) claims.getRoles();
            for (String role : roles) {
                boolean hasAccess = roleService.hasResourceAccess(role, uri);
                if (!hasAccess) {
                    throw new AppSecurityException(500, "User has no role access to URI " + uri);
                }
            }
            chain.doFilter(req, res);
        } catch (AppSecurityException sce) {
            httpRes.sendError(sce.getCode(), toJson(sce));
        }
    }

    private String toJson(AppSecurityException sce) {
        try {
            AppSecurityExceptionBean en = new AppSecurityExceptionBean();
            en.setCode(sce.getCode());
            en.setMessage(sce.getMessage());
            en.setType(sce.getClass().getName());
            return mapper.writeValueAsString(en);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
