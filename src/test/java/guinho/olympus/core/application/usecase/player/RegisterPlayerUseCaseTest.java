package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.abstractions.PasswordHasher;
import guinho.olympus.core.application.repository.player.PlayerMutation;
import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.usecase.player.dto.CreatePlayerDto;
import guinho.olympus.core.application.usecase.player.dto.ResponsePlayerDto;
import guinho.olympus.core.application.usecase.player.shared.exception.EmailAlreadyExistsException;
import guinho.olympus.core.application.usecase.player.shared.exception.NicknameAlreadyExistsException;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;
import guinho.olympus.core.domain.shared.InvalidArgumentException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegisterPlayerUseCaseTest {
    @Mock
    private PlayerMutation mutationService;
    @Mock
    private PlayerQuery queryService;
    @Mock
    private PasswordHasher hasher;

    @InjectMocks
    private RegisterPlayerUseCase registerUseCase;

    @Nested
    class RegisterPlayer {
        @Test
        public void shouldRegisterAPlayerWithSuccess() {
            CreatePlayerDto request = new CreatePlayerDto("nickname", "email@test.com", "Testing!123");

            Mockito.when(queryService.emailExists(Email.of(request.email()))).thenReturn(false);
            Mockito.when(queryService.nicknameExists(Nickname.of(request.nickname()))).thenReturn(false);
            Mockito.when(hasher.hash(Mockito.anyString())).thenReturn("hashed-password");

            Player player = Player.create(Nickname.of(request.nickname()), Email.of(request.email()), PasswordHash.of("hashed-password"));

            Mockito.when(mutationService.save(Mockito.any(Player.class))).thenReturn(player);

            ResponsePlayerDto response = registerUseCase.execute(request);

            assertNotNull(response);
            assertEquals(request.nickname(), response.nickname());
            assertEquals(request.email(), response.email());
            Mockito.verify(queryService).emailExists(Email.of(request.email()));
            Mockito.verify(queryService).nicknameExists(Nickname.of(request.nickname()));
            Mockito.verify(hasher).hash(request.password());
            Mockito.verify(mutationService).save(Mockito.any(Player.class));
        }
    }

    @Nested
    class Validations {
        @Test
        public void shouldThrowEmailAlreadyExistsExceptionWhenEmailAlreadyExists() {
            CreatePlayerDto request = new CreatePlayerDto("nickname", "email@test.com", "Testing!123");

            Mockito.when(queryService.emailExists(Email.of(request.email()))).thenReturn(true);

            Exception exception = assertThrows(EmailAlreadyExistsException.class, () -> registerUseCase.execute(request));

            assertEquals("Email already exists.", exception.getMessage());
            Mockito.verify(queryService).emailExists(Email.of(request.email()));
            Mockito.verify(queryService, Mockito.never()).nicknameExists(Nickname.of(request.nickname()));
            Mockito.verifyNoInteractions(hasher);
            Mockito.verifyNoInteractions(mutationService);
        }

        @Test
        public void shouldThrowNicknameAlreadyExistsExceptionWhenNicknameAlreadyExists() {
            CreatePlayerDto request = new CreatePlayerDto("nickname", "email@test.com", "Testing!123");

            Mockito.when(queryService.emailExists(Email.of(request.email()))).thenReturn(false);
            Mockito.when(queryService.nicknameExists(Nickname.of(request.nickname()))).thenReturn(true);

            Exception exception = assertThrows(NicknameAlreadyExistsException.class, ()-> registerUseCase.execute(request));

            assertEquals("Nickname already exists.", exception.getMessage());
            Mockito.verify(queryService).nicknameExists(Nickname.of(request.nickname()));
            Mockito.verifyNoInteractions(hasher);
            Mockito.verifyNoInteractions(mutationService);
        }

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenEmailIsInvalid() {
            CreatePlayerDto request = new CreatePlayerDto("nickname", "invalid", "Testing!123");

            Exception exception = assertThrows(InvalidArgumentException.class, () -> registerUseCase.execute(request));

            assertEquals("Invalid email format", exception.getMessage());
            Mockito.verifyNoInteractions(queryService);
            Mockito.verifyNoInteractions(hasher);
            Mockito.verifyNoInteractions(mutationService);
        }

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenNicknameIsInvalid() {
            CreatePlayerDto request = new CreatePlayerDto("", "email@test.com", "Testing!123");

            Exception exception = assertThrows(InvalidArgumentException.class, () -> registerUseCase.execute(request));

            assertEquals("Invalid nickname format", exception.getMessage());
            Mockito.verifyNoInteractions(queryService);
            Mockito.verifyNoInteractions(hasher);
            Mockito.verifyNoInteractions(mutationService);
        }

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenPasswordIsInvalid() {
            CreatePlayerDto request = new CreatePlayerDto("nickname", "email@test.com", "Testing");

            Exception exception = assertThrows(InvalidArgumentException.class, () -> registerUseCase.execute(request));

            assertEquals("Password does not meet complexity requirements", exception.getMessage());
            Mockito.verifyNoInteractions(queryService);
            Mockito.verifyNoInteractions(hasher);
            Mockito.verifyNoInteractions(mutationService);
        }

        @Test
        public void shouldThrowInvalidArgumentExceptionWhenPasswordHashIsInvalid() {
            CreatePlayerDto request = new CreatePlayerDto("nickname", "email@test.com", "Testing!123");

            Mockito.when(queryService.emailExists(Mockito.any())).thenReturn(false);
            Mockito.when(queryService.nicknameExists(Mockito.any())).thenReturn(false);
            Mockito.when(hasher.hash(Mockito.anyString())).thenReturn("");

            Exception exception = assertThrows(InvalidArgumentException.class, () -> registerUseCase.execute(request));

            assertEquals("Invalid hash", exception.getMessage());
            Mockito.verify(hasher).hash(request.password());
            Mockito.verifyNoInteractions(mutationService);
        }
    }

}