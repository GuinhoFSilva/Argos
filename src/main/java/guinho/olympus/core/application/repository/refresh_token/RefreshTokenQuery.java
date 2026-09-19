package guinho.olympus.core.application.repository.refresh_token;

import guinho.olympus.core.domain.refresh_token.RefreshToken;

import java.util.Optional;

public interface RefreshTokenQuery {
    Optional<RefreshToken> findByRefreshToken(String refreshTokenValue);
}
