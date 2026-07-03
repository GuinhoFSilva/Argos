package guinho.olympus.core.integration.auth;

import guinho.olympus.core.application.usecase.player.dto.CreatePlayerDto;
import guinho.olympus.core.application.usecase.player.dto.LoginInputDto;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;
import guinho.olympus.core.integration.IntegrationTest;
import guinho.olympus.infrastructure.persistence.JdbcPlayerRepository;
import guinho.olympus.infrastructure.security.BCryptPasswordHasherAdapter;
import guinho.olympus.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
@Transactional
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcPlayerRepository repository;

    @Autowired
    private BCryptPasswordHasherAdapter hasher;

    @Nested
    class RegisterPlayer {
        @Test
        public void shouldRegisterANewPlayer() throws Exception {
            CreatePlayerDto request = new CreatePlayerDto("nickname", "email@test.com", "StrongPassword!123");
            ObjectMapper objectMapper = new ObjectMapper();

            mockMvc.perform(post("/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.nickname").value(request.nickname()))
                    .andExpect(jsonPath("$.email").value(request.email()))
                    .andExpect(jsonPath("$.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.updatedAt").isNotEmpty());
        }
    }

    @Test
    public void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        Player player = Player.create(Nickname.of("nickname"), Email.of("email@test.com"), PasswordHash.of("Password-hash"));

        repository.save(player);

        CreatePlayerDto request = new CreatePlayerDto("GenericNickname", "email@test.com", "StrongPassword!123");
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldReturnConflictWhenNicknameAlreadyExists() throws Exception {
        Player player = Player.create(Nickname.of("NicknameExists"), Email.of("email@test.com"), PasswordHash.of("Password-hash"));

        repository.save(player);

        CreatePlayerDto request = new CreatePlayerDto("NicknameExists", "email@test.com", "StrongPassword!123");
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Nested
    class LoginPlayer {
        @Test
        public void shouldLoginPlayerWithSuccess() throws Exception {
            Player player = Player.create(Nickname.of("nickname"), Email.of("email@test.com"), PasswordHash.of(hasher.hash("StrongPassword!123")));

            repository.save(player);

            LoginInputDto request = new LoginInputDto("email@test.com", "StrongPassword!123");
            ObjectMapper objectMapper = new ObjectMapper();

            mockMvc.perform(post("/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        public void shouldReturnConflictWhenEmailIsWrong() throws Exception {
            Player player = Player.create(Nickname.of("nickname"), Email.of("email@test.com"), PasswordHash.of(hasher.hash("StrongPassword!123")));

            repository.save(player);

            LoginInputDto request = new LoginInputDto("wrongemail@test.com", "StrongPassword!123");
            ObjectMapper objectMapper = new ObjectMapper();

            mockMvc.perform(post("/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        public void shouldReturnConflictWhenPasswordIsWrong() throws Exception {
            Player player = Player.create(Nickname.of("nickname"), Email.of("email@test.com"), PasswordHash.of(hasher.hash("StrongPassword!123")));

            repository.save(player);

            LoginInputDto request = new LoginInputDto("email@test.com", "Incorrect!123");
            ObjectMapper objectMapper = new ObjectMapper();

            mockMvc.perform(post("/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }
}


