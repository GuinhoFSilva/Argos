package guinho.olympus.core.application.abstractions;

import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.domain.player.valueobject.Role;

import java.util.UUID;

public interface TokenProvider {
    String generateToken(AuthenticatedPlayer authenticatedPlayer);

    Role getRole(String token);

    boolean validateToken(String token, UUID userId);
}
