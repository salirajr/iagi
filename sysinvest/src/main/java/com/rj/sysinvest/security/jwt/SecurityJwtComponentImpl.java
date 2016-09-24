package com.rj.sysinvest.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Component
@Data
public class SecurityJwtComponentImpl implements SecurityJwtComponent {

    private String signatureAlgorithm = "HS512";
    private Key secretKey = MacProvider.generateKey();
    private String issuer = getClass().getName();
    private Integer expiresInMinutes = -1;//60 * 8; // 8 hours 
    private CompressionCodec compressionCodec = null;

    @Override
    public String buildJwt(SecurityClaims claims) {
        long now = System.currentTimeMillis();
        SignatureAlgorithm alg = SignatureAlgorithm.forName(signatureAlgorithm);
        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuedAt(new Date(now))
                .setIssuer(issuer)
                .setSubject(claims.getSubject())
                .claim(SecurityClaims.PROP_ROLES, claims.getRoles())
                .signWith(alg, secretKey);
        if (expiresInMinutes != null || expiresInMinutes > 0) {
            jwtBuilder.setExpiration(new Date(now + (expiresInMinutes * 60 * 1000)));
        }
        if (compressionCodec != null) {
            jwtBuilder.compressWith(compressionCodec);
        }
        return jwtBuilder.compact();
    }

    @Override
    public SecurityClaims parseJwt(String jwt) {
        JwtParser jwtParser = Jwts.parser().setSigningKey(secretKey);
        Jws<Claims> jws = jwtParser.parseClaimsJws(jwt);
        Claims claims = jws.getBody();
        SecurityClaims sc = new SecurityClaims();
        sc.setSubject(claims.getSubject());
        sc.setRoles((List<String>) claims.get(SecurityClaims.PROP_ROLES));
        return sc;
    }

}
