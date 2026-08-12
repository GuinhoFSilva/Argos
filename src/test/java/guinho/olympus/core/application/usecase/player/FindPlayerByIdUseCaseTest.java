package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.application.usecase.player.dto.ResponsePlayerDto;
import guinho.olympus.core.application.usecase.player.shared.exception.PlayerAccessDeniedException;
import guinho.olympus.core.application.usecase.player.shared.exception.ResourceNotFoundException;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;
import guinho.olympus.core.domain.player.valueobject.Role;
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
class FindPlayerByIdUseCaseTest {
    @Mock
    private PlayerQuery queryService;

    @InjectMocks
    private FindPlayerByIdUseCase findPlayerUseCase;

    @Nested
    class FindPlayer {
        @Test
        public void shouldFindAPlayerByIdWithSuccess() {
            UUID id = UUID.randomUUID();
            Player player = Player.reconstitute(id, Nickname.of("nickname"), Email.of("email@test.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"), LocalDateTime.now(), LocalDateTime.now());
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(id, player.getRole());

            Mockito.when(queryService.findById(id)).thenReturn(Optional.of(player));

            ResponsePlayerDto response = findPlayerUseCase.findById(id, authenticatedPlayer);

            assertNotNull(response);
            assertEquals(player.getId(), response.id());
            Mockito.verify(queryService).findById(id);
        }

        @Test
        public void shouldFindAnotherPlayerWhenItsAdmin() {
            Player player = Player.reconstitute(UUID.randomUUID(), Nickname.of("nickname"), Email.of("email@test.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"), LocalDateTime.now(), LocalDateTime.now());

            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(UUID.randomUUID(), Role.of("ADMIN"));

            Mockito.when(queryService.findById(player.getId())).thenReturn(Optional.of(player));

            ResponsePlayerDto response = findPlayerUseCase.findById(player.getId(), authenticatedPlayer);

            assertNotNull(response);
            assertEquals(player.getId(), response.id());
            Mockito.verify(queryService).findById(player.getId());
        }

        @Test
        public void shouldThrowResourceNotFoundExceptionWhenPlayerDoesNotExist() {
            UUID id = UUID.randomUUID();
            Mockito.when(queryService.findById(id)).thenReturn(Optional.empty());
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(id, Role.of("PLAYER"));

            Exception exception = assertThrows(ResourceNotFoundException.class, ()-> findPlayerUseCase.findById(id, authenticatedPlayer));
            assertEquals("Player Not Found", exception.getMessage());
            Mockito.verify(queryService).findById(id);
        }

        @Test
        public void shouldThrowPlayerAccessDeniedExceptionWhenIdsDoNotMatchAndItsNotAdmin() {
            UUID playerId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();
            Player player = Player.reconstitute(playerId, Nickname.of("nickname"), Email.of("email@test.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"), LocalDateTime.now(), LocalDateTime.now());
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(userId, player.getRole());

            Mockito.when(queryService.findById(playerId)).thenReturn(Optional.of(player));

            Exception exception = assertThrows(PlayerAccessDeniedException.class, ()-> findPlayerUseCase.findById(playerId, authenticatedPlayer));
            assertEquals("You do not have permission to access this player", exception.getMessage());
        }
    }

}