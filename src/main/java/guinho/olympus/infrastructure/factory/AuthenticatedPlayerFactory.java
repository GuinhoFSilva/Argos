package guinho.olympus.infrastructure.factory;

import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.domain.player.valueobject.Role;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthenticatedPlayerFactory {
    public AuthenticatedPlayer from(JwtAuthenticationToken token) {
        UUID authenticatedUserId = UUID.fromString(token.getName());
        String authenticatedRole = token.getToken().getClaimAsString("role");
        return new AuthenticatedPlayer(authenticatedUserId, Role.of(authenticatedRole));
    }
}
