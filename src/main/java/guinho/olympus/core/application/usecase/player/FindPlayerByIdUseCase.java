package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.usecase.player.shared.exception.ResourceNotFoundException;
import guinho.olympus.core.domain.player.Player;

import java.util.UUID;

public class FindPlayerByIdUseCase {
    private final PlayerQuery queryService;

    public FindPlayerByIdUseCase(PlayerQuery queryService) {
        this.queryService = queryService;
    }

    public Player findById(String id) {
        UUID uuid = UUID.fromString(id);

        return queryService.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException("Player Not Found")
        );
    }
}
