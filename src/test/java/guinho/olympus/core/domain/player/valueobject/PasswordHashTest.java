package guinho.olympus.core.domain.player.valueobject;

import guinho.olympus.core.domain.shared.InvalidArgumentException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashTest {
    @Nested
    class CreatePasswordHash {
        @Test
        public void shouldCreateAPasswordHashWithSuccess() {
            PasswordHash passwordHash = PasswordHash.of("TestingHash");

            assertNotNull(passwordHash);
            assertEquals("TestingHash", passwordHash.getValue());
        }
    }

    @Nested
    class Validations {
        @Test
        public void shouldThrownInvalidArgumentExceptionWhenValueIsBlank() {
            Exception exception = assertThrows(InvalidArgumentException.class, () -> PasswordHash.of(""));

            assertEquals("Invalid hash", exception.getMessage());
        }

        @Test
        public void shouldThrownInvalidArgumentExceptionWhenValueIsNull() {
            Exception exception = assertThrows(InvalidArgumentException.class, () -> PasswordHash.of(null));

            assertEquals("Invalid hash", exception.getMessage());
        }


    }

}