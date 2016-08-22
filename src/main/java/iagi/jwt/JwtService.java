package iagi.jwt;

import java.util.Map;

/**
 *
 * @author is
 */
public interface JwtService {

    String buildJwt(Map<String, Object> claims);

    Map<String, Object> parseJwt(String jwt);
}
