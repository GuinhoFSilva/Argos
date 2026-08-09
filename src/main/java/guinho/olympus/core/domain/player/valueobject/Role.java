package guinho.olympus.core.domain.player.valueobject;

import guinho.olympus.core.domain.shared.InvalidArgumentException;

import java.util.Objects;

public class Role {
    private final String value;

    private Role(String value) {
        this.value = value;
    }

    public static Role of(String value) {
        boolean matches = validRole(value);

        if (matches) {
            return new Role(value);
        }

        throw new InvalidArgumentException("Invalid role format");
    }

    private static boolean validRole(String value) {
        return value != null && !value.isBlank();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(value, role.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
