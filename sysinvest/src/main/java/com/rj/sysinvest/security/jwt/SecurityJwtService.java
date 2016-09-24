package com.rj.sysinvest.security.jwt;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public interface SecurityJwtService {

    String buildJwt(SecurityClaims claims);

    SecurityClaims parseJwt(String jwt);
}
