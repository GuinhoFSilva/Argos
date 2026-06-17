package guinho.olympus.core.domain.player.valueobject;

import guinho.olympus.core.domain.shared.InvalidArgumentException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NicknameTest {

    @Nested
    class CreateNickname {
        @Test
        public void shouldCreateANicknameWithSuccess() {
            Nickname nickname = Nickname.of("nickname");
            assertNotNull(nickname);
            assertEquals("nickname", nickname.getValue());
        }

        @Test
        public void shouldCreateANicknameWith20Characters() {
            Nickname nickname = Nickname.of("10111213141516171819");
            assertNotNull(nickname);
            assertEquals("10111213141516171819", nickname.getValue());
        }
    }

    @Nested
    class Validation {

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenValueIsNull() {
            Exception exception = assertThrows(InvalidArgumentException.class, () -> Nickname.of(null));

            assertEquals("Invalid nickname format", exception.getMessage());
        }

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenValueIsBlank() {
            Exception exception = assertThrows(InvalidArgumentException.class, () -> Nickname.of(""));

            assertEquals("Invalid nickname format", exception.getMessage());
        }

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenNicknameExceeds20Characters() {
            Exception exception = assertThrows(InvalidArgumentException.class, () -> Nickname.of("111213141516171819201"));

            assertEquals("Invalid nickname format", exception.getMessage());
        }


    }
}
