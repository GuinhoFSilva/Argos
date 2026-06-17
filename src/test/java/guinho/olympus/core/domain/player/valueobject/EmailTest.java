package guinho.olympus.core.domain.player.valueobject;

import guinho.olympus.core.domain.shared.InvalidArgumentException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class EmailTest {

    @Nested
    class CreateEmail {
        @Test
        public void shouldCreateAEmailWithSuccess() {
            Email email = Email.of("email@test.com");

            assertNotNull(email);
            assertEquals("email@test.com", email.getValue());
        }
    }

    @Nested
    class Validation {
        @Test
        public void shouldThrowInvalidArgumentExceptionWhenEmailIsBlank() {
            Exception exception = assertThrows(InvalidArgumentException.class, () -> Email.of(""));

            assertEquals("Invalid email format", exception.getMessage());
        }

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenEmailDoesNotContainAtSymbol() {
            Exception exception = assertThrows(InvalidArgumentException.class, () -> Email.of("test.com"));

            assertEquals("Invalid email format", exception.getMessage());
        }

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenEmailHasInvalidDomain() {
            Exception exception = assertThrows(InvalidArgumentException.class, () -> Email.of("test@"));

            assertEquals("Invalid email format", exception.getMessage());
        }

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenEmailHasNoTld() {
            Exception exception = assertThrows(InvalidArgumentException.class, ()-> Email.of("email@teste"));

            assertEquals("Invalid email format", exception.getMessage());
        }
    }
}
