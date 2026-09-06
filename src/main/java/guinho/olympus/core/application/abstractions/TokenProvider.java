package guinho.olympus.core.application.abstractions;

import guinho.olympus.core.application.security.AuthenticatedPlayer;

public interface TokenProvider {
    String generateToken(AuthenticatedPlayer authenticatedPlayer);
}
