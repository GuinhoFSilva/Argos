package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.repository.player.PlayerMutation;
import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.application.usecase.player.dto.ResponsePlayerDto;
import guinho.olympus.core.application.usecase.player.shared.exception.PermissionException;
import guinho.olympus.core.application.usecase.player.shared.exception.ResourceNotFoundException;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;
import guinho.olympus.core.domain.player.valueobject.Role;
import guinho.olympus.core.domain.shared.UnchangedFieldException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PromotePlayerToAdminUseCaseTest {
    @Mock
    private PlayerQuery playerQuery;

    @Mock
    private PlayerMutation playerMutation;

    @InjectMocks
    private PromotePlayerToAdminUseCase promoteUseCase;

    @Nested
    class PromotePlayer {
        @Test
        public void shouldPromoteAPlayerToAdminWithSuccess() {
            Player player = Player.reconstitute(UUID.randomUUID(), Nickname.of("nickname"), Email.of("email@test.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"), LocalDateTime.now(), LocalDateTime.now());
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(UUID.randomUUID(), Role.of("ADMIN"));

            Mockito.when(playerQuery.findById(player.getId())).thenReturn(Optional.of(player));

            Mockito.when(playerMutation.changeRole(player)).thenReturn(player);

            ResponsePlayerDto response = promoteUseCase.execute(player.getId(), authenticatedPlayer);

            assertNotNull(response);
            assertEquals("ADMIN", response.role());
            Mockito.verify(playerQuery).findById(player.getId());
            Mockito.verify(playerMutation).changeRole(player);
        }
    }

    @Nested
    class Validations {
        @Test
        public void shouldThrowResourceNotFoundExceptionWhenPlayerDoesNotExist() {
            UUID id = UUID.randomUUID();
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(UUID.randomUUID(), Role.of("ADMIN"));

            Mockito.when(playerQuery.findById(id)).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> promoteUseCase.execute(id, authenticatedPlayer));

            Mockito.verify(playerQuery).findById(id);
            Mockito.verifyNoInteractions(playerMutation);
            assertEquals("Player Not Found", exception.getMessage());
        }

        @Test
        public void shouldThrowPermissionExceptionWhenPlayerItsNotAdmin() {
            UUID id = UUID.randomUUID();
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(UUID.randomUUID(), Role.of("PLAYER"));

            Exception exception = assertThrows(PermissionException.class, () -> promoteUseCase.execute(id, authenticatedPlayer));

            Mockito.verifyNoInteractions(playerQuery);
            Mockito.verifyNoInteractions(playerMutation);
            assertEquals("You don't have permission to do this", exception.getMessage());
        }

        @Test
        public void shouldThrowUnchangedFieldExceptionWhenPlayerAlreadyIsAnAdmin() {
            UUID id = UUID.randomUUID();
            Player player = Player.reconstitute(id, Nickname.of("nickname"), Email.of("email@test.com"), PasswordHash.of("hashed-password"), Role.of("ADMIN"), LocalDateTime.now(), LocalDateTime.now());
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(UUID.randomUUID(), Role.of("ADMIN"));

            Mockito.when(playerQuery.findById(player.getId())).thenReturn(Optional.of(player));

            Exception exception = assertThrows(UnchangedFieldException.class, () -> promoteUseCase.execute(id, authenticatedPlayer));

            Mockito.verify(playerQuery).findById(id);
            Mockito.verifyNoInteractions(playerMutation);
            assertEquals("This user is already an admin", exception.getMessage());
        }
    }




}
