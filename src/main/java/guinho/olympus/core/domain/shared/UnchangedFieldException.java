package guinho.olympus.core.domain.shared;

public class UnchangedFieldException extends RuntimeException {
    public UnchangedFieldException(String message) {
        super(message);
    }
}
