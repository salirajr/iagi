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
public class SecurityRoleServiceImpl implements SecurityRoleService {

    @Autowired
    private SecurityRoleRepository roleRepo;

    @Override
    public boolean hasResourceAccess(String roleName, String uri) {
        SecurityRole role = roleRepo.findOne(roleName);
        return role.getResources().stream()
                .map(resource -> resource.getUriPrefix())
                .filter(uriPrefix -> matchUri(uriPrefix, uri))
                .findFirst()
                .isPresent();
    }

    private boolean matchUri(String uriPrefix, String uri) {
        return uri.startsWith(uriPrefix);
    }

}
