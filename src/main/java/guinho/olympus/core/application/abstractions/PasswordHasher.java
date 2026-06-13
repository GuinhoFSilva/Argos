package guinho.olympus.core.application.abstractions;

public interface PasswordHasher {
    String hash(String password);

    boolean verify(String password, String hash);
}
