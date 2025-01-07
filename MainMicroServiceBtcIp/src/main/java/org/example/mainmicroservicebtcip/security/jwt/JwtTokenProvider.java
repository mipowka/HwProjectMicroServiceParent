package org.example.mainmicroservicebtcip.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.web.embedded.jetty.JettyWebServer;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private SecretKey secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    private final Long EXPIRATION_TIME = 86400000L;

    public String generateToken(String username, List<String> roles) {
        Claims claims = Jwts.claims()
                .subject(username)
                .build();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
        Key key = Keys.hmacShaKeyFor(secretKey.getEncoded());

        return Jwts.builder()
                .claims(claims)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(new SecretKeySpec(secretKey.getEncoded(),"HmacSHA256"))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(new SecretKeySpec(secretKey.getEncoded(),"HmacSHA256"))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public List<String> getRolesFromToken(String token) {
        List<String> roles  = (List<String>) Jwts.parser()
                .verifyWith(new SecretKeySpec(secretKey.getEncoded(),"HmacSHA256"))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles");
        return roles;
    }
}
