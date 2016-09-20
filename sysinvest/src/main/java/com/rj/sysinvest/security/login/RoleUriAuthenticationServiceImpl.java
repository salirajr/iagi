package com.rj.sysinvest.security.login;

import com.rj.sysinvest.security.repo.SecurityRole;
import com.rj.sysinvest.security.repo.SecurityRoleRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Data
public class RoleUriAuthenticationServiceImpl implements RoleUriAuthenticationService {

    @Autowired
    private SecurityRoleRepository roleRepo;

    @Override
    public boolean hasAccess(String roleName, String uri) {
        SecurityRole role = roleRepo.findOne(roleName);
        return role.getResources().stream()
                .map(rsc -> rsc.getUriPattern())
                .filter(uriPattern -> uri.startsWith(uriPattern))
                .findFirst()
                .isPresent();
    }

}
