package guinho.olympus.core.application.usecase.player.mapper;

import guinho.olympus.core.application.usecase.player.dto.ResponsePlayerDto;
import guinho.olympus.core.domain.player.Player;

import java.util.List;

public class PlayerMapper {
    public static ResponsePlayerDto toResponse(Player entity) {
        if(entity == null){
            return null;
        }

        return new ResponsePlayerDto(
                entity.getId(),
                entity.getNickname().getValue(),
                entity.getEmail().getValue(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static List<ResponsePlayerDto> toResponse(List<Player> entities) {
        return entities.stream().map(PlayerMapper::toResponse).toList();
    }
}
