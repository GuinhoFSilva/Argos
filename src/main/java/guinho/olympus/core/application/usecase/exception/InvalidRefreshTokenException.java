package guinho.olympus.core.application.usecase.exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("Invalid Refresh Token");
    }
}
