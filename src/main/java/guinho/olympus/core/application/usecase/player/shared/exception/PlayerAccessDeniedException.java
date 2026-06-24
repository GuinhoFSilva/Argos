package guinho.olympus.core.application.usecase.player.shared.exception;

public class PlayerAccessDeniedException extends RuntimeException {
    public PlayerAccessDeniedException() {
        super("You do not have permission to access this player");
    }
}
