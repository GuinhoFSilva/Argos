package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.usecase.player.dto.ResponsePlayerDto;
import guinho.olympus.core.application.usecase.player.mapper.PlayerMapper;
import guinho.olympus.core.application.usecase.player.shared.exception.ResourceNotFoundException;
import guinho.olympus.core.domain.player.Player;

import java.util.UUID;

public class FindPlayerByIdUseCase {
    private final PlayerQuery queryService;

    public FindPlayerByIdUseCase(PlayerQuery queryService) {
        this.queryService = queryService;
    }

    public ResponsePlayerDto findById(UUID id) {
        Player player = queryService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Player Not Found")
        );

        return PlayerMapper.toResponse(player);
    }
}
