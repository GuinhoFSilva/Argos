package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.abstractions.PasswordHasher;
import guinho.olympus.core.application.abstractions.TokenProvider;
import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.usecase.player.dto.CreatePlayerDto;
import guinho.olympus.core.application.usecase.player.dto.LoginInputDto;
import guinho.olympus.core.application.usecase.player.dto.LoginResponseDto;
import guinho.olympus.core.application.usecase.player.shared.exception.InvalidCredentialsException;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.Password;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;
import guinho.olympus.core.domain.shared.InvalidArgumentException;
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
class LoginPlayerUseCaseTest {
    @Mock
    private PlayerQuery queryService;
    @Mock
    private PasswordHasher hasher;
    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private LoginPlayerUseCase loginUseCase;

    @Nested
    class LoginPlayer {
        @Test
        void shouldAuthenticatePlayerWhenCredentialsAreValid() {
            LoginInputDto request = new LoginInputDto("email@test.com", "Testing!123");
            Player player = Player.reconstitute(UUID.randomUUID(), Nickname.of("nickname"), Email.of(request.email()), PasswordHash.of(request.password()), LocalDateTime.now(), LocalDateTime.now());

            Mockito.when(queryService.findByEmail(Email.of(request.email()))).thenReturn(Optional.of(player));

            Mockito.when(hasher.verify(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

            Mockito.when(tokenProvider.generateToken(Mockito.any(UUID.class))).thenReturn("token");

            LoginResponseDto response = loginUseCase.execute(request);

            assertNotNull(response);
            Mockito.verify(queryService).findByEmail(Email.of(request.email()));
            Mockito.verify(tokenProvider).generateToken(player.getId());
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
            LoginInputDto request = new LoginInputDto("email@test.com", "Testing!123");
            Player player = Player.reconstitute(UUID.randomUUID(), Nickname.of("nickname"), Email.of(request.email()), PasswordHash.of("Testing!321"), LocalDateTime.now(), LocalDateTime.now());

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