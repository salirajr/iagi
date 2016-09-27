package com.rj.sysinvest.security.rest;

import com.rj.sysinvest.dao.StaffRepository;
import com.rj.sysinvest.model.Staff;
import com.rj.sysinvest.security.jwt.SecurityClaims;
import com.rj.sysinvest.security.login.LoginService;
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
import com.rj.sysinvest.security.jwt.SecurityJwtComponent;

@RestController
@RequestMapping("/login-api")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

//    @Resource(name = "LoginServiceTestImpl")
    @Resource(name = "LoginServiceImpl")
    private LoginService loginService;

    @Resource
    private StaffRepository staffRepo;

    @Resource
    private SecurityJwtComponent jwtService;

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
        String token = jwtService.buildJwt(new SecurityClaims(userName, roles));

        Staff staff = staffRepo.findByUserLoginUserName(userName);

        System.out.println(staff);

        if (staff != null) {
            loginResponse.setFullName(staff.getFullName());
            loginResponse.setRank(staff.getRank().getName());
        }

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
        private String fullName;
        private String rank;

    }

    @Data
    public static class UserLogin {

        private String username;
        private String password;

    }
}
