package guinho.olympus.core.domain.player.valueobject;

import guinho.olympus.core.domain.shared.InvalidArgumentException;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {
    private final String value;
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private Email(String value){
        this.value = value;
    }

    public static Email of(String value) {
        boolean matches = value.matches(EMAIL.pattern());

        if(matches) {
            return new Email(value);
        }

        throw new InvalidArgumentException("Invalid email format");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
