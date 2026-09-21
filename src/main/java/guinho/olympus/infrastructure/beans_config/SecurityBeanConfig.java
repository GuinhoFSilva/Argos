package guinho.olympus.infrastructure.beans_config;

import guinho.olympus.infrastructure.security.RefreshTokenProviderImpl;
import guinho.olympus.core.application.usecase.refresh_token.RefreshTokenUseCase;
import guinho.olympus.infrastructure.persistence.JdbcPlayerRepository;
import guinho.olympus.infrastructure.persistence.JdbcRefreshTokenRepository;
import guinho.olympus.infrastructure.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityBeanConfig {

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(JdbcRefreshTokenRepository jdbcTokenAdapter, JdbcPlayerRepository jdbcPlayerAdapter, JwtTokenProvider tokenAdapter, RefreshTokenProviderImpl refreshTokenAdapter) {
        return new RefreshTokenUseCase(jdbcTokenAdapter, jdbcTokenAdapter, jdbcPlayerAdapter, tokenAdapter, refreshTokenAdapter);
    }
}
