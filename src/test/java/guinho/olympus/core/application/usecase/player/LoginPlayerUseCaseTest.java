package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.abstractions.Hasher;
import guinho.olympus.core.application.abstractions.RefreshTokenProvider;
import guinho.olympus.core.application.abstractions.TokenProvider;
import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.repository.refresh_token.RefreshTokenMutation;
import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.application.usecase.player.dto.LoginInputDto;
import guinho.olympus.core.application.usecase.player.dto.LoginResponseDto;
import guinho.olympus.core.application.usecase.exception.InvalidCredentialsException;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.*;
import guinho.olympus.core.domain.refresh_token.RefreshToken;
import guinho.olympus.core.domain.shared.InvalidArgumentException;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginPlayerUseCaseTest {
    @Mock
    private PlayerQuery queryService;
    @Mock
    private Hasher hasher;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private RefreshTokenMutation refreshTokenMutation;
    @Mock
    private RefreshTokenProvider refreshTokenProvider;

    @InjectMocks
    private LoginPlayerUseCase loginUseCase;

    @Nested
    class LoginPlayer {
        @Test
        void shouldAuthenticatePlayerWhenCredentialsAreValid() {
            LoginInputDto request = new LoginInputDto("email@test.com", "Testing!123");
            Player player = PlayerFactory.createValidPlayer();

            RefreshToken refreshToken = RefreshToken.create(player.getId(), "NewToken", LocalDateTime.now().plusDays(7), false);

            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(player.getId(), player.getRole());

            Mockito.when(queryService.findByEmail(Email.of(request.email()))).thenReturn(Optional.of(player));

            Mockito.when(hasher.verify(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

            Mockito.when(tokenProvider.generateToken(Mockito.any(AuthenticatedPlayer.class))).thenReturn("token");

            Mockito.when(refreshTokenProvider.generateRefreshToken(Mockito.any(AuthenticatedPlayer.class))).thenReturn(refreshToken);

            Mockito.when(refreshTokenMutation.save(Mockito.any(RefreshToken.class))).thenReturn(refreshToken);


            LoginResponseDto response = loginUseCase.execute(request);

            assertNotNull(response);
            Mockito.verify(queryService).findByEmail(Email.of(request.email()));
            Mockito.verify(tokenProvider).generateToken(authenticatedPlayer);
            Mockito.verify(refreshTokenProvider).generateRefreshToken(authenticatedPlayer);
            Mockito.verify(hasher).verify(request.password(), player.getPasswordHash().getValue());
        }
    }

    @Nested
    class Validations {
        @Test
        public void shouldThrowInvalidCredentialsExceptionWhenPlayerIsNotFound() {
            LoginInputDto request = new LoginInputDto("email@test.com", "Testing!123");

            Mockito.when(queryService.findByEmail(Email.of(request.email()))).thenReturn(Optional.empty());
            Exception exception = assertThrows(InvalidCredentialsException.class, () -> loginUseCase.execute(request));

            assertEquals("Invalid email or password.", exception.getMessage());
            Mockito.verify(queryService).findByEmail(Email.of(request.email()));
            Mockito.verifyNoInteractions(tokenProvider);
            Mockito.verifyNoInteractions(hasher);
        }

        @Test
        public void shouldThrowInvalidCredentialsExceptionWhenPasswordIsIncorrect() {
            LoginInputDto request = new LoginInputDto("email@test.com", "Testing!321");
            Player player = PlayerFactory.createValidPlayer();

            Mockito.when(queryService.findByEmail(Email.of(request.email()))).thenReturn(Optional.of(player));
            Mockito.when(hasher.verify(request.password(), player.getPasswordHash().getValue())).thenReturn(false);
            Exception exception = assertThrows(InvalidCredentialsException.class, () -> loginUseCase.execute(request));

            assertEquals("Invalid email or password.", exception.getMessage());
            Mockito.verify(queryService).findByEmail(Email.of(request.email()));
            Mockito.verify(hasher).verify(request.password(), player.getPasswordHash().getValue());
            Mockito.verifyNoInteractions(tokenProvider);
        }

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenEmailIsInvalid() {
            LoginInputDto request = new LoginInputDto("invalid", "Testing!123");

            Exception exception = assertThrows(InvalidArgumentException.class, () -> loginUseCase.execute(request));

            assertEquals("Invalid email format", exception.getMessage());
            Mockito.verifyNoInteractions(queryService);
            Mockito.verifyNoInteractions(hasher);
        }

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenPasswordIsInvalid() {
            LoginInputDto request = new LoginInputDto("email@test.com", "Testing");

            Exception exception = assertThrows(InvalidArgumentException.class, () -> loginUseCase.execute(request));

            assertEquals("Password does not meet complexity requirements", exception.getMessage());
            Mockito.verifyNoInteractions(queryService);
            Mockito.verifyNoInteractions(hasher);
        }
    }


}