package com.rj.sysinvest.rest.user;



import java.util.List;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public interface UserService {

    boolean isValidUsername(String name);

    List<String> findRolesByUsername(String name);

    boolean authenticate(String name, String password);
}
