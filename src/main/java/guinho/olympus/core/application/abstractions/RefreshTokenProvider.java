package guinho.olympus.core.application.abstractions;

import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.domain.refresh_token.RefreshToken;

public interface RefreshTokenProvider {
    RefreshToken generateRefreshToken(AuthenticatedPlayer authenticatedPlayer);
}
