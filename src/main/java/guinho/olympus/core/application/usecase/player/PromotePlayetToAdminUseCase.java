package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.repository.player.PlayerMutation;
import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.application.usecase.player.dto.ResponsePlayerDto;
import guinho.olympus.core.application.usecase.player.mapper.PlayerMapper;
import guinho.olympus.core.application.usecase.player.shared.exception.PermissionException;
import guinho.olympus.core.application.usecase.player.shared.exception.ResourceNotFoundException;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Role;

import java.util.UUID;

public class PromotePlayetToAdminUseCase {
    private final PlayerQuery playerQuery;
    private final PlayerMutation playerMutation;

    public PromotePlayetToAdminUseCase(PlayerQuery playerQuery, PlayerMutation playerMutation) {
        this.playerQuery = playerQuery;
        this.playerMutation = playerMutation;
    }

    public ResponsePlayerDto execute(UUID playerId, AuthenticatedPlayer authenticatedPlayer) {
        if(!authenticatedPlayer.role().isAdmin()) {
            throw new PermissionException("You don't have permission to do this");
        }

        Player player = playerQuery.findById(playerId).orElseThrow(()-> new ResourceNotFoundException("Player Not Found"));

        player.promoteToAdmin(Role.of("ADMIN"));
        playerMutation.changeRole(player);

        return PlayerMapper.toResponse(player);
    }
}
