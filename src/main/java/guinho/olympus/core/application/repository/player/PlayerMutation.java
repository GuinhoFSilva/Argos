package guinho.olympus.core.application.repository.player;

import guinho.olympus.core.domain.player.Player;

import java.util.UUID;

public interface PlayerMutation {
    Player save(Player player);

    Player update(Player player);
}
