package guinho.olympus.infrastructure.security;

import guinho.olympus.core.application.abstractions.Hasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class BCryptPasswordHasherAdapter implements Hasher {
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
