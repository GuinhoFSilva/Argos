package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.usecase.player.dto.ResponsePlayerDto;
import guinho.olympus.core.application.usecase.player.mapper.PlayerMapper;
import guinho.olympus.core.domain.player.Player;

import java.util.List;

public class FindAllPlayersUseCase {
    private final PlayerQuery queryService;

    public FindAllPlayersUseCase(PlayerQuery queryService) {
        this.queryService = queryService;
    }

    public List<ResponsePlayerDto> findAll() {
        List<Player> players = queryService.findAll();

        return PlayerMapper.toResponse(players);
    }
}
