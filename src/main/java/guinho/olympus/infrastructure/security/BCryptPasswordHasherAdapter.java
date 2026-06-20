package guinho.olympus.infrastructure.security;

import guinho.olympus.core.application.abstractions.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordHasherAdapter implements PasswordHasher {
    private final BCryptPasswordEncoder encoder;

    public BCryptPasswordHasherAdapter(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String hash(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean verify(String password, String hash) {
        return encoder.matches(password, hash);
    }
}
