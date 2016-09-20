package com.rj.sysinvest.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Component
@Data
public class JwtServiceImpl implements JwtService {

    private String signatureAlgorithm = "HS512";
    private Key secretKey = MacProvider.generateKey();
    private String issuer = getClass().getName();
    private Integer expiresInMinutes = 60 * 8; // 8 hours 

    @Override
    public String buildJwt(String userName, List<String> roles) {
        long now = System.currentTimeMillis();
        SignatureAlgorithm alg = SignatureAlgorithm.forName(signatureAlgorithm);
        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuedAt(new Date(now))
                .setIssuer(issuer)
                .setSubject(userName)
                .claim("roles", roles)
                .signWith(alg, secretKey);
        if (expiresInMinutes != null || expiresInMinutes > 0) {
            jwtBuilder.setExpiration(new Date(now + (expiresInMinutes * 60 * 1000)));
        }
        return jwtBuilder.compact();
    }

    @Override
    public Map<String, Object> parseJwt(String jwt) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(secretKey);
        Jws<Claims> jws = jwtParser.parseClaimsJws(jwt);
        return jws.getBody();
    }

}
