package guinho.olympus.infrastructure.security;

import guinho.olympus.core.application.abstractions.RefreshTokenProvider;
import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.domain.refresh_token.RefreshToken;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Repository
public class RefreshTokenProviderImpl implements RefreshTokenProvider {
    public RefreshToken generateRefreshToken(AuthenticatedPlayer authenticatedPlayer) {
        return RefreshToken.create(
                authenticatedPlayer.playerId(),
                UUID.randomUUID().toString(),
                LocalDateTime.ofInstant(Instant.now().plus(7, ChronoUnit.DAYS),  ZoneOffset.UTC),
                false
        );
    }
}
