package guinho.olympus.core.domain.player.valueobject;

import guinho.olympus.core.domain.shared.InvalidArgumentException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Nested
    class CreateRole {
        @Test
        public void shouldCreateARoleWithSuccess() {
            Role role = Role.of("ADMIN");
            assertNotNull(role);
            assertEquals("ADMIN", role.getValue());
        }
    }

    @Nested
    class Validation {
        @Test
        public void shouldThrowInvalidArgumentExceptionWhenValueIsNull() {
            Exception exception = assertThrows(InvalidArgumentException.class, () -> Role.of(null));
            assertEquals("Invalid role format", exception.getMessage());
        }

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenValueIsBlank() {
            Exception exception = assertThrows(InvalidArgumentException.class, () -> Role.of(""));
            assertEquals("Invalid role format", exception.getMessage());
        }
    }
}
