package com.rj.sysinvest.login;



import java.util.List;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public interface LoginService {

    boolean isValidUsername(String name);

    List<String> findRolesByUsername(String name);

    boolean authenticate(String name, String password);
}
