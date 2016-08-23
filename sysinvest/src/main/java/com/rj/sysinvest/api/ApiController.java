package com.rj.sysinvest.api;



import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @RequestMapping(value = "role/{role}", method = RequestMethod.GET)
    public Boolean hasRole(@PathVariable String role, HttpServletRequest request)
            throws ServletException {

        Map claims = (Map) request.getAttribute("claims");
        List<String> roles = (List<String>) claims.get("roles");
        return roles.contains(role);
    }

    @RequestMapping(value = "userroles/{username}", method = RequestMethod.GET)
    public List<String> getUserRoles(@PathVariable String username, HttpServletRequest request) {
        Map claims = (Map) request.getAttribute("claims");
        return (List<String>) claims.get("roles");
    }

}
