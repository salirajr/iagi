package com.rj.sysinvest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rj.sysinvest.security.repo.SecurityResource;
import com.rj.sysinvest.security.repo.SecurityResourceRepository;
import com.rj.sysinvest.security.repo.SecurityRole;
import com.rj.sysinvest.security.repo.SecurityRoleRepository;
import com.rj.sysinvest.security.repo.SecurityUser;
import com.rj.sysinvest.security.repo.SecurityUserRepository;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Transactional
public class SetupSecurityData {

    @Resource
    SecurityResourceRepository resourceRepo;
    @Resource
    SecurityRoleRepository roleRepo;
    @Resource
    SecurityUserRepository userRepo;

    @PostConstruct
    @Transactional
    void setup() throws JsonProcessingException {
        SecurityResource resource = new SecurityResource();
        resource.setUriPrefix("/api/");
        if (!resourceRepo.exists(resource.getUriPrefix())) {
            resourceRepo.save(resource);
        }
        resource = resourceRepo.findOne(resource.getUriPrefix());

        SecurityRole role = new SecurityRole();
        role.setRoleName("admin");
        role.setResources(Arrays.asList(resource));
        if (!roleRepo.exists(role.getRoleName())) {
            roleRepo.save(role);
        }
        role = roleRepo.findOne(role.getRoleName());

        SecurityUser user = new SecurityUser();
        user.setUserName("admin");
        user.setPassword("admin");
        user.setRoles(Arrays.asList(role));
        if (!userRepo.exists(user.getUserName())) {
            userRepo.save(user);
        }
        user = userRepo.findOne(user.getUserName());
    }
}
