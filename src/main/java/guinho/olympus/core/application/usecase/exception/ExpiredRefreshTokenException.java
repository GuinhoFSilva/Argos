package guinho.olympus.core.application.usecase.exception;

public class ExpiredRefreshTokenException extends RuntimeException {
    public ExpiredRefreshTokenException() {
        super("Refresh token has expired.");
    }
}
