package guinho.olympus.core.application.repository.player;

import guinho.olympus.core.domain.player.Player;

public interface PlayerMutation {
    Player save(Player player);

    Player update(Player player);
}
