package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.usecase.player.dto.ResponsePlayerDto;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FindAllPlayersUseCaseTest {
    @Mock
    private PlayerQuery queryService;

    @InjectMocks
    private FindAllPlayersUseCase findPlayerUseCase;

    @Nested
    class FindAll {
        @Test
        public void shouldFindAllPlayersWithSuccess() {
            List<Player> players = List.of(Player.create(Nickname.of("nickname"), Email.of("email@test.com"), PasswordHash.of("hashed-password")));

            Mockito.when(queryService.findAll()).thenReturn(players);
            List<ResponsePlayerDto> response = findPlayerUseCase.findAll();

            assertNotNull(response);
            assertEquals(1, response.size());
            assertEquals(players.getFirst().getId(), response.getFirst().id());
            Mockito.verify(queryService).findAll();
        }

        @Test
        public void shouldReturnEmptyListWhenNoPlayersExist() {
            Mockito.when(queryService.findAll()).thenReturn(List.of());

            List<ResponsePlayerDto> response = findPlayerUseCase.findAll();
            assertNotNull(response);
            assertTrue(response.isEmpty());
            Mockito.verify(queryService).findAll();
        }
    }


}