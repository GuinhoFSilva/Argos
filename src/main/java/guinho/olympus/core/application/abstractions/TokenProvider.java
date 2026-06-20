package guinho.olympus.core.application.abstractions;

import java.util.UUID;

public interface TokenProvider {
    String generateToken(UUID userId);

    boolean validateToken(String token, UUID userId);
}
