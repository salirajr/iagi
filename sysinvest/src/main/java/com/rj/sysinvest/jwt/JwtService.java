package com.rj.sysinvest.jwt;

import java.util.Map;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public interface JwtService {

    String buildJwt(Map<String, Object> claims);

    Map<String, Object> parseJwt(String jwt);
}
