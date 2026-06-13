package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.usecase.player.shared.exception.ResourceNotFoundException;
import guinho.olympus.core.domain.player.Player;

import java.util.List;
import java.util.UUID;

public class FindAllPlayerUseCase {
    private final PlayerQuery queryService;

    public FindAllPlayerUseCase(PlayerQuery queryService) {
        this.queryService = queryService;
    }

    public List<Player> findAll() {
        return queryService.findAll();
    }
}
