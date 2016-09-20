package com.rj.sysinvest.security.login;

import com.rj.sysinvest.security.repo.SecurityRole;
import com.rj.sysinvest.security.repo.SecurityUser;
import java.util.List;
import javax.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import com.rj.sysinvest.security.repo.SecurityUserRepository;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service("LoginServiceImpl")
@Data
public class LoginServiceImpl implements LoginService {

    @Resource
    private SecurityUserRepository userRepo;

    @Override
    public boolean isValidUsername(String name) {
        return userRepo.findOne(name) != null;
    }

    @Override
    public List<String> findRolesByUsername(String name) {
        SecurityUser user = userRepo.findOne(name);
        if (user == null) {
            throw new RuntimeException("User '" + name + "' does not exist");
        }
        return user.getRoles().stream()
                .map(SecurityRole::getRoleName)
                .collect(Collectors.toList());
    }

    @Override
    public boolean authenticate(String name, String password) {
        SecurityUser user = userRepo.findOne(name);
        return password.equals(user.getPassword());
    }

}
