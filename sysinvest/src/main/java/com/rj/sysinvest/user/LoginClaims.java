package com.rj.sysinvest.user;

import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author rais
 */
public class LoginClaims extends LinkedHashMap<String, Object> {

    public static final String KEY_SUBJECT = "sub",
            KEY_ROLES = "roles";

    public void setSubject(String subject) {
        put(KEY_SUBJECT, subject);
    }

    public String getSubject() {
        return (String) get(KEY_SUBJECT);
    }

    public void setRoles(List<String> roles) {
        put(KEY_ROLES, roles);
    }

    public List<String> getRoles() {
        return (List<String>) get(KEY_ROLES);
    }

}
