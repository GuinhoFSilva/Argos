package guinho.olympus.infrastructure.security;

import guinho.olympus.core.application.abstractions.TokenProvider;
import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.domain.player.valueobject.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Repository;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Repository
public class JwtTokenProvider implements TokenProvider {
    private final SecretKey signingKey;

    public JwtTokenProvider() {
        String secret = System.getenv("JWT_SECRET");

        if(secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET not defined");
        }

        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(AuthenticatedPlayer authenticatedPlayer) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + 3600000);
        UUID playerId = authenticatedPlayer.playerId();
        Role playerRole = authenticatedPlayer.role();

        return Jwts.builder()
                .subject(playerId.toString())
                .claim("role", playerRole.getValue())
                .signWith(signingKey, Jwts.SIG.HS256)
                .issuedAt(now)
                .expiration(exp)
                .compact();
    }

    @Override
    public Role getRole(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return Role.of(claims.get("role", String.class));
    }

    @Override
    public boolean validateToken(String token, UUID userId) {
        Claims claims = getAllClaimsFromToken(token);
        String tokenUserId = claims.getSubject();
        return tokenUserId.equals(userId.toString()) && claims.getExpiration().after(new Date());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token).getPayload();
    }
}
