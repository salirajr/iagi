package com.rj.sysinvest.rest.user;

import ch.qos.logback.core.CoreConstants;
import com.rj.sysinvest.jwt.JwtService;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import lombok.Data;

@RestController
@RequestMapping("/login-api")
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private JwtService jwtService;

    @RequestMapping(method = RequestMethod.POST)
    public LoginResponse post(@RequestBody UserLogin userLogin)
            throws ServletException {
        LoginResponse loginResponse = new LoginResponse();
        
        System.out.println("Service called by "+userLogin.username);

        if (userLogin.getUsername() == null || userLogin.getPassword() == null
                || !userService.isValidUsername(userLogin.getUsername())
                || !userService.authenticate(userLogin.getUsername(), userLogin.getPassword())) {
            loginResponse.setMessage("Invalid login");
            loginResponse.setStatus("FAIL");
            return loginResponse;
        }
        String subject = userLogin.getUsername();
        List<String> roles = userService.findRolesByUsername(subject);
        LoginClaims claims = new LoginClaims();
        claims.setSubject(subject);
        claims.setRoles(roles);
        String token = jwtService.buildJwt(claims);
        loginResponse.setMessage("Successfully login");
        loginResponse.setStatus("SUCCESS");
        loginResponse.setToken(token);
        return loginResponse;
    }

    @Data
    public static class LoginResponse {

        private String token;
        private String status;
        private String message;

    }

    @Data
    public static class UserLogin {

        private String username;
        private String password;
        
    }
}
