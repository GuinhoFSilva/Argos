package guinho.olympus.core.application.usecase.refresh_token;

import guinho.olympus.core.application.abstractions.TokenProvider;
import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.repository.refresh_token.RefreshTokenMutation;
import guinho.olympus.core.application.repository.refresh_token.RefreshTokenProvider;
import guinho.olympus.core.application.repository.refresh_token.RefreshTokenQuery;
import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.application.usecase.exception.ExpiredRefreshTokenException;
import guinho.olympus.core.application.usecase.exception.InvalidRefreshTokenException;
import guinho.olympus.core.application.usecase.exception.ResourceNotFoundException;
import guinho.olympus.core.application.usecase.player.dto.LoginResponseDto;
import guinho.olympus.core.application.usecase.refresh_token.dto.RefreshTokenRequest;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.refresh_token.RefreshToken;

import java.util.UUID;

public class RefreshTokenUseCase {
    private final RefreshTokenQuery refreshTokenService;
    private final RefreshTokenMutation refreshTokenMutation;
    private final PlayerQuery playerService;
    private final TokenProvider tokenProvider;

    public RefreshTokenUseCase(RefreshTokenQuery refreshTokenService, RefreshTokenMutation refreshTokenMutation, PlayerQuery playerService, TokenProvider tokenProvider) {
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenMutation = refreshTokenMutation;
        this.playerService = playerService;
        this.tokenProvider = tokenProvider;
    }

    public LoginResponseDto execute(RefreshTokenRequest request) {
        RefreshToken oldRefreshToken = refreshTokenService.findByRefreshToken(request.refreshToken()).orElseThrow(() -> new ResourceNotFoundException("Refresh Token Not Found"));

        if(oldRefreshToken.isExpired()) {
            throw new ExpiredRefreshTokenException();
        }

        if(oldRefreshToken.isRevoked()) {
            throw new InvalidRefreshTokenException();
        }

        UUID playerId = oldRefreshToken.getPlayerId();
        Player player = playerService.findById(playerId).orElseThrow(() -> new ResourceNotFoundException("Player Not Found."));

        AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(player.getId(), player.getRole());

        oldRefreshToken.revoke();

        refreshTokenMutation.revoke(oldRefreshToken);

        RefreshToken refreshToken = RefreshTokenProvider.generateRefreshToken(authenticatedPlayer);

        refreshTokenMutation.save(refreshToken);

        String accessToken = tokenProvider.generateToken(authenticatedPlayer);

        return new LoginResponseDto(accessToken, refreshToken.getToken());
    }
}
