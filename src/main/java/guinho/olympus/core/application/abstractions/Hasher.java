package guinho.olympus.core.application.abstractions;

public interface Hasher {
    String hash(String value);

    boolean verify(String value, String hash);
}
