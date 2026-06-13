package guinho.olympus.core.domain.player.valueobject;

import guinho.olympus.core.domain.shared.InvalidArgumentException;

import java.util.regex.Pattern;

public class Password {
    private final String value;
    private static final Pattern PASSWORD = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$");

    private Password(String value) {
        this.value = value;
    }

    public static Password of(String value) {
        boolean matches = value.matches(PASSWORD.pattern());

        if (matches) {
             return new Password(value);
        }

        throw new InvalidArgumentException("Password does not meet complexity requirements");
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "*********";
    }
}
