package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.abstractions.PasswordHasher;
import guinho.olympus.core.application.abstractions.TokenProvider;
import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.application.usecase.player.dto.LoginInputDto;
import guinho.olympus.core.application.usecase.player.dto.LoginResponseDto;
import guinho.olympus.core.application.usecase.player.shared.exception.InvalidCredentialsException;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Password;

import java.util.UUID;

public class LoginPlayerUseCase {
    private final PlayerQuery queryService;
    private final PasswordHasher hasher;
    private final TokenProvider tokenProvider;

    public LoginPlayerUseCase(PlayerQuery queryService, PasswordHasher hasher, TokenProvider tokenProvider) {
        this.queryService = queryService;
        this.hasher = hasher;
        this.tokenProvider = tokenProvider;
    }

    public LoginResponseDto execute(LoginInputDto command) {
        Email validEmail = Email.of(command.email());
        Password validPassword = Password.of(command.password());

        Player player = queryService.findByEmail(validEmail).orElseThrow(InvalidCredentialsException::new);

        if (!hasher.verify(validPassword.getValue(), player.getPasswordHash().getValue())) {
            throw new InvalidCredentialsException();
        }

        AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(player.getId(), player.getRole());

        String token = tokenProvider.generateToken(authenticatedPlayer);

        return new LoginResponseDto(token);
    }
}
