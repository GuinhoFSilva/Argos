package guinho.olympus.core.domain.player.valueobject;

import guinho.olympus.core.domain.shared.InvalidArgumentException;

import java.util.Objects;

public class Nickname {
    private final String value;

    private Nickname(String value) {
        this.value = value;
    }

    public static Nickname of(String value) {
        Boolean matches = validNickname(value);

        if(matches) {
            return new Nickname(value);
        }

        throw new InvalidArgumentException("Invalid nickname format");
    }

    private static Boolean validNickname (String value) {
        return value != null && value.length() <= 20;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Nickname nickname = (Nickname) o;
        return Objects.equals(value, nickname.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
