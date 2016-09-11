package com.rj.sysinvest.rest.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
public class UserServiceImpl implements UserService {

    private final Map<String, Map<String, Object>> userDb = new HashMap<>();

    public UserServiceImpl() {

        Map userData = new HashMap();
        userData.put("password", "12345");
        userData.put("roles", Arrays.asList("user"));
        userDb.put("user", userData);

        userData = new HashMap();
        userData.put("password", "12345");
        userData.put("roles", Arrays.asList("user", "admin"));
        userDb.put("admin", userData);

    }

    @Override
    public boolean isValidUsername(String name) {
        return userDb.containsKey(name);
    }

    @Override
    public List<String> findRolesByUsername(String name) {
        Map userData = userDb.get(name);
        if (userData != null) {
            return (List<String>) userData.get("roles");
        }
        return null;
    }

    @Override
    public boolean authenticate(String name, String password) {
        Map userData = userDb.get(name);
        if (userData != null) {
            return password.equals(userData.get("password"));
        }
        return false;
    }

}
