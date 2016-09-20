package com.rj.sysinvest.security.login;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public interface RoleUriAuthenticationService {

    boolean hasAccess(String roleName, String uri);

}
