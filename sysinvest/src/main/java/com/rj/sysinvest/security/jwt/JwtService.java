package com.rj.sysinvest.security.jwt;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public interface JwtService {

    String buildJwt(String userName, List<String> roles);

    Map<String, Object> parseJwt(String jwt);
}
