package guinho.olympus.support;

import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.enums.Rank;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;
import guinho.olympus.core.domain.player.valueobject.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerFactory {
    public static Player createValidPlayer() {
        return Player.reconstitute(UUID.randomUUID(), Nickname.of("nickname"), Email.of("email@test.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"), Rank.BRONZE, LocalDateTime.now(), LocalDateTime.now());
    }

    public static Player reconstituteValidPlayer(UUID id) {
        return Player.reconstitute(id, Nickname.of("nickname"), Email.of("email@test.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"), Rank.BRONZE, LocalDateTime.now(), LocalDateTime.now());

    }
}
