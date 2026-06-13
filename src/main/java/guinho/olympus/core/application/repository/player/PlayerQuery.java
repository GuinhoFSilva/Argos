package guinho.olympus.core.application.repository.player;

import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerQuery {
    List<Player> findAll();

    Optional<Player> findById(UUID id);
    Optional<Player> findByEmail(Email email);

    boolean emailExists(Email email);
    boolean nicknameExists(Nickname nickname);


}
