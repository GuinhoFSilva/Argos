package guinho.olympus.integration;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MySQLContainer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdbcTestContainer implements BeforeAllCallback {
    private static AtomicBoolean containerStarted = new AtomicBoolean(false);

    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test_argos")
            .withUsername("test_user")
            .withPassword("root");

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if(!containerStarted.get()){
            mysql.start();

            containerStarted.set(true);
        }
    }
}
