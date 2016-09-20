package com.rj.sysinvest.security.jwt;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public class JwtClaims extends HashMap<String, Object> {

    public static final String KEY_ROLES = "roles";

    public List<String> getRoles() {
        return (List<String>) get(KEY_ROLES);
    }

//    public 
}
