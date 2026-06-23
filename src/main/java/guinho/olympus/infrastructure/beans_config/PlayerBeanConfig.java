package guinho.olympus.infrastructure.beans_config;

import guinho.olympus.core.application.usecase.player.FindAllPlayersUseCase;
import guinho.olympus.core.application.usecase.player.FindPlayerByIdUseCase;
import guinho.olympus.core.application.usecase.player.LoginPlayerUseCase;
import guinho.olympus.core.application.usecase.player.RegisterPlayerUseCase;
import guinho.olympus.infrastructure.persistence.JdbcPlayerRepository;
import guinho.olympus.infrastructure.security.BCryptPasswordHasherAdapter;
import guinho.olympus.infrastructure.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlayerBeanConfig {

    @Bean
    public RegisterPlayerUseCase registerPlayerUseCase(JdbcPlayerRepository jdbcAdapter, BCryptPasswordHasherAdapter passwordHasherAdapter) {
        return new RegisterPlayerUseCase(jdbcAdapter, jdbcAdapter, passwordHasherAdapter);
    }

    @Bean
    public LoginPlayerUseCase loginPlayerUseCase(JdbcPlayerRepository jdbcAdapter, BCryptPasswordHasherAdapter passwordHasherAdapter, JwtTokenProvider tokenAdapter) {
        return new LoginPlayerUseCase(jdbcAdapter, passwordHasherAdapter, tokenAdapter);
    }

    @Bean
    public FindPlayerByIdUseCase findPlayerByIdUseCase(JdbcPlayerRepository jdbcAdapter) {
        return new FindPlayerByIdUseCase(jdbcAdapter);
    }

    @Bean
    public FindAllPlayersUseCase findAllPlayersUseCase(JdbcPlayerRepository jdbcAdapter) {
        return new FindAllPlayersUseCase(jdbcAdapter);
    }

}
