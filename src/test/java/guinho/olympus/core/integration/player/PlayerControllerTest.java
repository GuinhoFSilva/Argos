package guinho.olympus.core.integration.player;

import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;
import guinho.olympus.core.integration.IntegrationTest;
import guinho.olympus.infrastructure.persistence.JdbcPlayerRepository;
import guinho.olympus.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcPlayerRepository repository;

    @Autowired
    private JwtTokenProvider tokenProvider;


    @Test
    public void shouldReturnPlayerByIdWhenAuthenticated() throws Exception {
        Player player = Player.create(Nickname.of("Nickname"), Email.of("test@email.com"), PasswordHash.of("hashed-password"));

        Player saved = repository.save(player);

        String token = tokenProvider.generateToken(saved.getId());

        mockMvc.perform(
                        get("/v1/players/" + saved.getId()).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId().toString()))
                .andExpect(jsonPath("$.nickname").value(saved.getNickname().getValue()))
                .andExpect(jsonPath("$.email").value(saved.getEmail().getValue()));
    }

    @Test
    public void shouldReturnForbiddenWhenAccessingAnotherPlayer() throws Exception {
        Player player = Player.create(Nickname.of("Nickname"), Email.of("test@email.com"), PasswordHash.of("hashed-password"));

        Player saved = repository.save(player);

        String token = tokenProvider.generateToken(UUID.randomUUID());

        mockMvc.perform(
                        get("/v1/players/" + saved.getId()).header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnUnauthorizedWhenTokenIsMissing() throws Exception {
        Player player = Player.create(Nickname.of("Nickname"), Email.of("test@email.com"), PasswordHash.of("hashed-password"));

        Player saved = repository.save(player);

        mockMvc.perform(
                        get("/v1/players/" + saved.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnUnauthorizedWhenTokenIsInvalid() throws Exception {
        Player player = Player.create(Nickname.of("Nickname"), Email.of("test@email.com"), PasswordHash.of("hashed-password"));

        Player saved = repository.save(player);

        mockMvc.perform(
                        get("/v1/players/" + saved.getId()).header("Authorization", "Bearer " + "invalid-Token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnNotFoundWhenPlayerDoesNotExist() throws Exception {
        Player player = Player.create(Nickname.of("Nickname"), Email.of("test@email.com"), PasswordHash.of("hashed-password"));

        String token = tokenProvider.generateToken(repository.save(player).getId());

        mockMvc.perform(
                        get("/v1/players/" + UUID.randomUUID()).header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

}
