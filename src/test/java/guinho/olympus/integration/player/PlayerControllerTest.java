package guinho.olympus.integration.player;

import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;
import guinho.olympus.core.domain.player.valueobject.Role;
import guinho.olympus.integration.IntegrationTest;
import guinho.olympus.infrastructure.persistence.JdbcPlayerRepository;
import guinho.olympus.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@IntegrationTest
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcPlayerRepository repository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM players");
    }

    @Nested
    class FindById {
        @Test
        public void shouldReturnPlayerByIdWhenAuthenticated() throws Exception {
            Player player = Player.create(Nickname.of("Nickname"), Email.of("test@email.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"));
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(player.getId(), player.getRole());
            Player saved = repository.save(player);

            String token = tokenProvider.generateToken(authenticatedPlayer);

            mockMvc.perform(
                            get("/v1/players/" + saved.getId()).header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(saved.getId().toString()))
                    .andExpect(jsonPath("$.nickname").value(saved.getNickname().getValue()))
                    .andExpect(jsonPath("$.email").value(saved.getEmail().getValue()));
        }

        @Test
        public void shouldReturnAnotherPlayerWhenItsAdmin() throws Exception {
            Player player = Player.create(Nickname.of("Nickname"), Email.of("test@email.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"));
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(UUID.randomUUID(), Role.of("ADMIN"));
            Player saved = repository.save(player);

            String token = tokenProvider.generateToken(authenticatedPlayer);

            mockMvc.perform(
                            get("/v1/players/" + saved.getId()).header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(saved.getId().toString()))
                    .andExpect(jsonPath("$.nickname").value(saved.getNickname().getValue()))
                    .andExpect(jsonPath("$.email").value(saved.getEmail().getValue()));
        }

        @Test
        public void shouldReturnForbiddenWhenAccessingAnotherPlayerAndItsNotAdmin() throws Exception {
            Player player = Player.create(Nickname.of("Nickname"), Email.of("test@email.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"));
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(UUID.randomUUID(), player.getRole());
            Player saved = repository.save(player);

            String token = tokenProvider.generateToken(authenticatedPlayer);

            mockMvc.perform(
                            get("/v1/players/" + saved.getId()).header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());
        }

        @Test
        public void shouldReturnUnauthorizedWhenTokenIsMissing() throws Exception {
            Player player = Player.create(Nickname.of("Nickname"), Email.of("test@email.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"));

            Player saved = repository.save(player);

            mockMvc.perform(
                            get("/v1/players/" + saved.getId()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        public void shouldReturnUnauthorizedWhenTokenIsInvalid() throws Exception {
            Player player = Player.create(Nickname.of("Nickname"), Email.of("test@email.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"));

            Player saved = repository.save(player);

            mockMvc.perform(
                            get("/v1/players/" + saved.getId()).header("Authorization", "Bearer " + "invalid-Token"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        public void shouldReturnNotFoundWhenPlayerDoesNotExist() throws Exception {
            Player player = Player.create(Nickname.of("Nickname"), Email.of("test@email.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"));
            repository.save(player);
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(player.getId(), player.getRole());
            String token = tokenProvider.generateToken(authenticatedPlayer);

            mockMvc.perform(
                            get("/v1/players/" + UUID.randomUUID()).header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    class FindAll {
        @Test
        public void shouldReturnPlayersWhenItsAdmin() throws Exception {
            Player player = Player.create(Nickname.of("Nickname"), Email.of("test@email.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"));
            Player anotherPlayer = Player.create(Nickname.of("Nickname2"), Email.of("test2@email.com"), PasswordHash.of("hashed-password"), Role.of("PLAYER"));

            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(UUID.randomUUID(), Role.of("ADMIN"));
            Player savedPlayer = repository.save(player);
            Player savedAnotherPlayer = repository.save(anotherPlayer);

            String token = tokenProvider.generateToken(authenticatedPlayer);

            mockMvc.perform(
                            get("/v1/players").header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[*].id", containsInAnyOrder(savedPlayer.getId().toString(), savedAnotherPlayer.getId().toString())))
                    .andExpect(jsonPath("$[*].nickname", containsInAnyOrder(savedPlayer.getNickname().getValue(), savedAnotherPlayer.getNickname().getValue())))
                    .andExpect(jsonPath("$[*].email", containsInAnyOrder(savedPlayer.getEmail().getValue(), savedAnotherPlayer.getEmail().getValue())))
                    .andExpect(jsonPath("$[*].role", containsInAnyOrder(savedPlayer.getRole().getValue(), savedAnotherPlayer.getRole().getValue())));
        }

        @Test
        public void shouldReturnEmptyListWhenNoPlayersExist() throws Exception {
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(UUID.randomUUID(), Role.of("ADMIN"));

            String token = tokenProvider.generateToken(authenticatedPlayer);

            mockMvc.perform(
                    get("/v1/players").header("Authorization", "Bearer " + token))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void shouldReturnForbiddenWhenItsNotAdmin() throws Exception {
            AuthenticatedPlayer authenticatedPlayer = new AuthenticatedPlayer(UUID.randomUUID(), Role.of("PLAYER"));

            String token = tokenProvider.generateToken(authenticatedPlayer);

            mockMvc.perform(
                            get("/v1/players").header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());
        }

        @Test
        public void shouldReturnUnauthorizedWhenTokenIsInvalid() throws Exception {
            mockMvc.perform(
                            get("/v1/players").header("Authorization", "Bearer " + "invalid-Token"))
                    .andExpect(status().isUnauthorized());
        }
    }
}
