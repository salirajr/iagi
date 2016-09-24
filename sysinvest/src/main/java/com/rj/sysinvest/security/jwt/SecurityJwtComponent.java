package com.rj.sysinvest.security.jwt;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public interface SecurityJwtComponent {

    String buildJwt(SecurityClaims claims);

    SecurityClaims parseJwt(String jwt);
}
