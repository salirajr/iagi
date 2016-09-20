package com.rj.sysinvest.security.rest;

import com.rj.sysinvest.security.login.LoginService;
import com.rj.sysinvest.security.jwt.JwtService;
import java.util.List;
import javax.servlet.ServletException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/login-api")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

//    @Resource(name = "LoginServiceTestImpl")
    @Resource(name = "LoginServiceImpl")
    private LoginService loginService;

    @Resource
    private JwtService jwtService;

    @RequestMapping(method = RequestMethod.POST)
    public LoginResponse post(@RequestBody UserLogin userLogin)
            throws ServletException {
        LoginResponse loginResponse = new LoginResponse();

        logger.debug("Service called by {}", userLogin.username);

        if (userLogin.getUsername() == null || userLogin.getPassword() == null
                || !loginService.isValidUsername(userLogin.getUsername())
                || !loginService.authenticate(userLogin.getUsername(), userLogin.getPassword())) {
            loginResponse.setMessage("Invalid login");
            loginResponse.setStatus("FAIL");
            return loginResponse;
        }
        String userName = userLogin.getUsername();
        List<String> roles = loginService.findRolesByUsername(userName);
        String token = jwtService.buildJwt(userName, roles);

        loginResponse.setRoles(roles);
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
        private List<String> roles;

    }

    @Data
    public static class UserLogin {

        private String username;
        private String password;

    }
}
