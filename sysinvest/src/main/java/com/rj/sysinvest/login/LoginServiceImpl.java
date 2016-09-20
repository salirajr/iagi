package com.rj.sysinvest.login;

import com.rj.sysinvest.model.Role;
import com.rj.sysinvest.model.User;
import java.util.List;
import javax.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Service;
import com.rj.sysinvest.dao.UserRepository;
import java.util.stream.Collectors;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service("LoginServiceImpl")
@Data
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserRepository userRepo;

    @Override
    public boolean isValidUsername(String name) {
        return userRepo.findOne(name) != null;
    }

    @Override
    public List<String> findRolesByUsername(String name) {
        User user = userRepo.findOne(name);
        if (user == null) {
            throw new RuntimeException("User '" + name + "' does not exist");
        }
        return user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
    }

    @Override
    public boolean authenticate(String name, String password) {
        User user = userRepo.findOne(name);
        return password.equals(user.getPassword());
    }

}
