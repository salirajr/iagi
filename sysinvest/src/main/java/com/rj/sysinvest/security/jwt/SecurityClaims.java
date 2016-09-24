package com.rj.sysinvest.security.jwt;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityClaims {

    private String subject;
    private List<String> roles;
    public static final String PROP_ROLES = "roles";
}
