package guinho.olympus.core.domain.refresh_token;

import java.time.LocalDateTime;
import java.util.UUID;

public class RefreshToken {
    private final UUID id;
    private final UUID playerId;
    private final String token;
    private final LocalDateTime expiresAt;
    private boolean revoked;

    private RefreshToken(UUID id, UUID playerId, String token, LocalDateTime expiresAt, boolean revoked) {
        this.id = id;
        this.playerId = playerId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }

    public static RefreshToken create(UUID playerId, String token, LocalDateTime expiresAt, boolean revoked) {
        return new RefreshToken(UUID.randomUUID(), playerId, token, expiresAt, revoked);
    }

    public static RefreshToken reconstitute(UUID id, UUID playerId, String token, LocalDateTime expiresAt, boolean revoked) {
        return new RefreshToken(id, playerId, token, expiresAt, revoked);
    }

    public void revoke() {
        this.revoked = true;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }
}
