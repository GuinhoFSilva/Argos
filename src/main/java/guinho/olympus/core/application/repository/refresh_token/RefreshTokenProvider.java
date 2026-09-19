package guinho.olympus.core.application.repository.refresh_token;

import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.domain.refresh_token.RefreshToken;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class RefreshTokenProvider {
    public static RefreshToken generateRefreshToken(AuthenticatedPlayer authenticatedPlayer) {
        return RefreshToken.create(
                authenticatedPlayer.playerId(),
                UUID.randomUUID().toString(),
                LocalDateTime.ofInstant(Instant.now().plus(7, ChronoUnit.DAYS),  ZoneOffset.UTC),
                false
        );
    }
}
