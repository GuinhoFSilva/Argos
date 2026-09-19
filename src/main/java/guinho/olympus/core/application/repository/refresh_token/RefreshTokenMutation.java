package guinho.olympus.core.application.repository.refresh_token;

import guinho.olympus.core.domain.refresh_token.RefreshToken;

public interface RefreshTokenMutation {
    RefreshToken save(RefreshToken refreshToken);

    void revoke(RefreshToken refreshToken);
}
