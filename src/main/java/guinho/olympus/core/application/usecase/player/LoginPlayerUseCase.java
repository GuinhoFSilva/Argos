package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.abstractions.Hasher;
import guinho.olympus.core.application.abstractions.TokenProvider;
import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.repository.refresh_token.RefreshTokenMutation;
import guinho.olympus.core.application.provider.RefreshTokenProvider;
import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.application.usecase.player.dto.LoginInputDto;
import guinho.olympus.core.application.usecase.player.dto.LoginResponseDto;
import guinho.olympus.core.application.usecase.exception.InvalidCredentialsException;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Password;
import guinho.olympus.core.domain.refresh_token.RefreshToken;

public class LoginPlayerUseCase {
    private final PlayerQuery queryService;
    private final RefreshTokenMutation refreshTokenService;
    private final Hasher hasher;
    private final TokenProvider tokenProvider;

    public LoginPlayerUseCase(PlayerQuery queryService, RefreshTokenMutation refreshTokenService, Hasher hasher, TokenProvider tokenProvider) {
        this.queryService = queryService;
        this.refreshTokenService = refreshTokenService;
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

        RefreshToken refreshToken = RefreshTokenProvider.generateRefreshToken(authenticatedPlayer);

        refreshTokenService.save(refreshToken);

        String token = tokenProvider.generateToken(authenticatedPlayer);

        return new LoginResponseDto(token, refreshToken.getToken());
    }


}
