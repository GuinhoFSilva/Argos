package guinho.olympus.core.domain.player.valueobject;

import guinho.olympus.core.domain.shared.InvalidArgumentException;

import java.util.Objects;

public class PasswordHash {
    private final String value;

    private PasswordHash(String value) {
        this.value = value;
    }

    public static PasswordHash of(String value) {
        if(value == null || value.isBlank()){
            throw new InvalidArgumentException("Invalid hash");
        }

        return new PasswordHash(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PasswordHash that = (PasswordHash) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "*******";
    }
}
