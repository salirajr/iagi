package com.rj.sysinvest.security.login;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public interface SecurityRoleService {

    boolean hasResourceAccess(String roleName, String uri);

}
