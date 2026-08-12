package guinho.olympus.core.application.security;

import guinho.olympus.core.domain.player.valueobject.Role;

import java.util.UUID;

public record AuthenticatedPlayer(UUID playerId, Role role) {
}
