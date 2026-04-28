package net.engineeringdigest.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private final String SECRET = "mysecretkey123"; // move to env in real systems

    private final long EXPIRATION = 1000 * 30 * 1; // 30 mins

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

        return Jwts.parser()
                .setSigningKey(SECRET)
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