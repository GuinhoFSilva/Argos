package guinho.olympus.core.application.usecase.refresh_token;

import guinho.olympus.core.application.abstractions.TokenProvider;
import guinho.olympus.core.application.usecase.exception.ExpiredRefreshTokenException;
import guinho.olympus.core.application.usecase.exception.InvalidCredentialsException;
import guinho.olympus.core.application.usecase.exception.InvalidRefreshTokenException;
import guinho.olympus.core.application.usecase.exception.ResourceNotFoundException;
import guinho.olympus.core.application.usecase.player.dto.LoginInputDto;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.infrastructure.security.RefreshTokenProviderImpl;
import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.repository.refresh_token.RefreshTokenMutation;
import guinho.olympus.core.application.repository.refresh_token.RefreshTokenQuery;
import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.application.usecase.player.dto.LoginResponseDto;
import guinho.olympus.core.application.usecase.refresh_token.dto.RefreshTokenRequest;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.refresh_token.RefreshToken;
import guinho.olympus.support.PlayerFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenUseCaseTest {

    @Mock
    private RefreshTokenQuery refreshTokenService;

    @Mock
    private RefreshTokenMutation refreshTokenMutation;

    @Mock
    private PlayerQuery playerService;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RefreshTokenProviderImpl refreshTokenProvider;

    @InjectMocks
    private RefreshTokenUseCase refreshTokenUseCase;


    @Nested
    class RefreshTokenTest {
        @Test
        void shouldRefreshTokenWithSuccess() {
            RefreshTokenRequest request = new RefreshTokenRequest("Token");
            UUID playerId = UUID.randomUUID();
            Player player = PlayerFactory.reconstituteValidPlayer(playerId);
            LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(10);
            RefreshToken oldRefreshToken = RefreshToken.create(playerId, "Token", expirationDate, false);
            RefreshToken newRefreshToken = RefreshToken.create(playerId, "NewToken", expirationDate.plusDays(7), false);
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(player.getId(), player.getRole());

            Mockito.when(refreshTokenService.findByRefreshToken(request.refreshToken())).thenReturn(Optional.of(oldRefreshToken));

            Mockito.when(playerService.findById(playerId)).thenReturn(Optional.of(player));

            Mockito.when(refreshTokenProvider.generateRefreshToken(Mockito.any(AuthenticatedPlayer.class))).thenReturn(newRefreshToken);

            Mockito.when(refreshTokenMutation.save(Mockito.any(RefreshToken.class))).thenReturn(newRefreshToken);

            Mockito.when(tokenProvider.generateToken(Mockito.any(AuthenticatedPlayer.class))).thenReturn("NewToken");

            LoginResponseDto response = refreshTokenUseCase.execute(request);


            assertNotNull(response);
            Mockito.verify(refreshTokenService).findByRefreshToken(request.refreshToken());
            Mockito.verify(playerService).findById(playerId);
            Mockito.verify(refreshTokenProvider).generateRefreshToken(authenticatedPlayer);
            Mockito.verify(refreshTokenMutation).revoke(oldRefreshToken);
            Mockito.verify(refreshTokenMutation).save(newRefreshToken);
            Mockito.verify(tokenProvider).generateToken(authenticatedPlayer);
        }
    }

    @Nested
    class Validations {
        @Test
        public void shouldThrowExpiredRefreshTokenExceptionWhenTokenIsExpired() {
            RefreshTokenRequest request = new RefreshTokenRequest("Token");
            RefreshToken expiredRefreshToken = RefreshToken.create(UUID.randomUUID(), "Token", LocalDateTime.now().minusMinutes(10), false);

            Mockito.when(refreshTokenService.findByRefreshToken(request.refreshToken())).thenReturn(Optional.of(expiredRefreshToken));

            ExpiredRefreshTokenException exception = assertThrows(ExpiredRefreshTokenException.class, () -> refreshTokenUseCase.execute(request));

            assertEquals("Refresh token has expired.", exception.getMessage());
            Mockito.verify(refreshTokenService).findByRefreshToken("Token");
            Mockito.verifyNoInteractions(playerService);
            Mockito.verifyNoInteractions(refreshTokenMutation);
            Mockito.verifyNoInteractions(refreshTokenProvider);
            Mockito.verifyNoInteractions(tokenProvider);
        }

        @Test
        public void shouldThrowResourceNotFoundExceptionWhenTokenNotFound() {
            RefreshTokenRequest request = new RefreshTokenRequest("Token");

            Mockito.when(refreshTokenService.findByRefreshToken(request.refreshToken())).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> refreshTokenUseCase.execute(request));

            assertEquals("Refresh Token Not Found.", exception.getMessage());
            Mockito.verify(refreshTokenService).findByRefreshToken("Token");
            Mockito.verifyNoInteractions(playerService);
            Mockito.verifyNoInteractions(refreshTokenMutation);
            Mockito.verifyNoInteractions(refreshTokenProvider);
            Mockito.verifyNoInteractions(tokenProvider);
        }

        @Test
        public void shouldThrowInvalidRefreshTokenExceptionWhenTokenIsRevoked() {
            RefreshTokenRequest request = new RefreshTokenRequest("Token");
            RefreshToken revokedRefreshToken = RefreshToken.create(UUID.randomUUID(), "Token", LocalDateTime.now().plusMinutes(10), true);

            Mockito.when(refreshTokenService.findByRefreshToken(request.refreshToken())).thenReturn(Optional.of(revokedRefreshToken));

            InvalidRefreshTokenException exception = assertThrows(InvalidRefreshTokenException.class, () -> refreshTokenUseCase.execute(request));

            assertEquals("Invalid Refresh Token", exception.getMessage());
            Mockito.verify(refreshTokenService).findByRefreshToken("Token");
            Mockito.verifyNoInteractions(playerService);
            Mockito.verifyNoInteractions(refreshTokenMutation);
            Mockito.verifyNoInteractions(refreshTokenProvider);
            Mockito.verifyNoInteractions(tokenProvider);
        }

        @Test
        public void shouldThrowResourceNotFoundExceptionWhenPlayerNotFound() {
            RefreshTokenRequest request = new RefreshTokenRequest("Token");
            UUID playerId = UUID.randomUUID();
            RefreshToken oldRefreshToken = RefreshToken.create(playerId, "Token", LocalDateTime.now().plusMinutes(10), false);

            Mockito.when(refreshTokenService.findByRefreshToken(request.refreshToken())).thenReturn(Optional.of(oldRefreshToken));

            Mockito.when(playerService.findById(playerId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> refreshTokenUseCase.execute(request));

            assertEquals("Player Not Found.", exception.getMessage());
            Mockito.verify(refreshTokenService).findByRefreshToken("Token");
            Mockito.verify(playerService).findById(playerId);
            Mockito.verifyNoInteractions(refreshTokenMutation);
            Mockito.verifyNoInteractions(refreshTokenProvider);
            Mockito.verifyNoInteractions(tokenProvider);
        }
    }

}