package guinho.olympus.infrastructure.beans_config;

import guinho.olympus.core.application.abstractions.PasswordHasher;
import guinho.olympus.infrastructure.security.BCryptPasswordHasherAdapter;
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
    public PasswordHasher passwordHasher(BCryptPasswordEncoder encoder) {
        return new BCryptPasswordHasherAdapter(encoder);
    }
}
