package unsa.sistemas.identityservice.Utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
    private SecretKey key;
    // Initializes the key after the class is instantiated and the jwtSecret is injected,
    // preventing the repeated creation of the key and enhancing performance
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String getUsernameFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date getExpirationFromToken(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationFromToken(token).before(new Date());
    }


    public String generateToken(UserDetails userDetails) {
        return doGenerateToken(userDetails.getUsername());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return doGenerateRefreshToken( userDetails.getUsername());
    }

    private String doGenerateToken(String subject) {
        Header<?> header = Jwts.header();
        header.setType("JWT");
        return Jwts.builder()
                .setHeader((Map<String, Object>) header)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs * 1000L))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // refresh token
    private String doGenerateRefreshToken( String subject) {
        Header<?> header = Jwts.header();
        header.setType("JWT");
        return Jwts.builder()
                .setHeader((Map<String, Object>) header)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (long) jwtExpirationMs * 2 * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        if (username != null) {
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }
        return false;
    }
}