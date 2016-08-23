package com.rj.sysinvest.user;



import java.util.List;

/**
 *
 * @author is
 */
public interface UserService {

    boolean isValidUsername(String name);

    List<String> findRolesByUsername(String name);

    boolean authenticate(String name, String password);
}
