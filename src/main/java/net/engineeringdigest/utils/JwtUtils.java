package net.engineeringdigest.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
//    private final SecretKey SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final String SECRET = "ngaPzm05hn9GAwLE0rhCcTxiIoWQ8kw4vEBCDDeC5n5"; // move to env in real systems

    private final long EXPIRATION = 1000 * 60 * 1; // 30 mins

    // GENERATE TOKEN
    public String generateToken(Long uid, String email, String role) {

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(email)
                .claim("uid", uid)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    // VALIDATE TOKEN
    public Claims validateToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // EXTRACT HELPERS
    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    public String getRole(Claims claims) {
        return claims.get("role", String.class);
    }

    public Long getUid(Claims claims) {
        return claims.get("uid", Long.class);
    }
}