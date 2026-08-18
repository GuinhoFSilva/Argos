package guinho.olympus.core.domain.player;

import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;
import guinho.olympus.core.domain.player.valueobject.Role;
import guinho.olympus.core.domain.shared.UnchangedFieldException;
import guinho.olympus.support.PlayerFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class PlayerTest {

    @Nested
    class Create {
        @Test
        public void shouldCreateAPlayerWithSuccess() {
            Player player = PlayerFactory.createValidPlayer();

            assertNotNull(player);
            assertEquals("nickname", player.getNickname().getValue());
            assertEquals("email@test.com", player.getEmail().getValue());
            assertEquals("hashed-password", player.getPasswordHash().getValue());
            assertEquals("PLAYER", player.getRole().getValue());
            assertNotNull(player.getCreatedAt());
            assertNotNull(player.getUpdatedAt());
        }
    }

    @Nested
    class Rename {
        @Test
        public void shouldRenameAPlayerWithSuccess() {
            Nickname newNickname = Nickname.of("nickname2");
            Player player = PlayerFactory.createValidPlayer();
            player.renameTo(newNickname);
            assertEquals(newNickname.getValue(), player.getNickname().getValue());
        }

        @Test
        public void shouldThrowUnchangedFieldExceptionWhenNickameIsTheSame() {
            Nickname newNickname = Nickname.of("nickname");
            Player player = PlayerFactory.createValidPlayer();

            Exception exception = assertThrows(UnchangedFieldException.class, () -> player.renameTo(newNickname));

            assertEquals("The new nickname must be different from the current nickname", exception.getMessage());
        }
    }

    @Nested
    class ChangeEmail {
        @Test
        public void shouldChangeAPlayerEmailWithSuccess() {
            Email newEmail = Email.of("email2@test.com");
            Player player = PlayerFactory.createValidPlayer();

            player.changeEmail(newEmail);

            assertEquals(newEmail.getValue(), player.getEmail().getValue());
        }

        @Test
        public void shouldThrowUnchangedFieldExceptionWhenEmailIsTheSame() {
            Email newEmail = Email.of("email@test.com");
            Player player = PlayerFactory.createValidPlayer();

            Exception exception = assertThrows(UnchangedFieldException.class, () -> player.changeEmail(newEmail));

            assertEquals("The new email must be different from the current email", exception.getMessage());
        }
    }

}
