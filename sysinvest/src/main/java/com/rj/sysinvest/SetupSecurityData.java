package com.rj.sysinvest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rj.sysinvest.dao.StaffRepository;
import com.rj.sysinvest.security.repo.SecurityRole;
import com.rj.sysinvest.security.repo.SecurityRoleRepository;
import com.rj.sysinvest.security.repo.SecurityUser;
import com.rj.sysinvest.security.repo.SecurityUserRepository;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Transactional
public class SetupSecurityData {

    private static final Logger logger = LoggerFactory.getLogger(SetupSecurityData.class);

//    @Resource
//    SecurityResourceRepository resourceRepo;
    @Resource
    SecurityRoleRepository roleRepo;
    @Resource
    SecurityUserRepository userRepo;
    @Resource
    StaffRepository staffRepo;

    @PostConstruct
    @Transactional
    void setup() throws JsonProcessingException {
//        SecurityResource resource = new SecurityResource();
//        resource.setUriPrefix("/api/");
//        if (!resourceRepo.exists(resource.getUriPrefix())) {
//            logger.debug("Insert resource {}", resource.getUriPrefix());
//            resource= resourceRepo.save(resource);
//        } 

        SecurityRole role = new SecurityRole();
        role.setRoleName("admin");
        role.setResources(Arrays.asList("/api/", "/air/", "/tanah/", "/udara/"));
        if (!roleRepo.exists(role.getRoleName())) {
            logger.debug("Insert role {}", role.getRoleName());
            role = roleRepo.save(role);
        } 

        SecurityUser user = new SecurityUser();
        user.setUserName("admin");
        user.setPassword("admin");
        user.setRoles(Arrays.asList(role));
        if (!userRepo.exists(user.getUserName())) {
            logger.debug("Insert user {}", user.getUserName());
            user = userRepo.save(user);
        }
        
        final SecurityRole staffRole = role;
        staffRepo.findAll().forEach(staff -> {
            if (staff.getUserLogin() == null) {
                String[] names = staff.getFullName().toLowerCase().trim().split(" ");
                String userName = names[0];
                for (int i = 1; userRepo.exists(userName); i++) {
                    userName += i;
                }
                SecurityUser userLogin = new SecurityUser();
                userLogin.setUserName(userName);
                userLogin.setPassword("password");
                userLogin.setRoles(Arrays.asList(staffRole));
                logger.debug("Insert user {}", userLogin.getUserName());
                userRepo.save(userLogin);
                staff.setUserLogin(userLogin);
                logger.debug("Insert staff {}", staff.getFullName());
                staff = staffRepo.save(staff);
            }
        });
    }
}
