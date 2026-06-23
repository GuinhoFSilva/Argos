package guinho.olympus.infrastructure.persistence;

import guinho.olympus.core.application.repository.player.PlayerMutation;
import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.usecase.player.shared.exception.ResourceNotFoundException;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcPlayerRepository implements PlayerQuery, PlayerMutation {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_PLAYER = "SELECT id, nickname, email, password_hash, created_at, updated_at FROM players";

    public JdbcPlayerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Player> playerRowMapper = ((rs, rowNum) -> Player.reconstitute(
            UUID.fromString(rs.getString("id")),
            Nickname.of(rs.getString("nickname")),
            Email.of(rs.getString("email")),
            PasswordHash.of(rs.getString("password_hash")),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
    ));

    @Override
    public Player save(Player player) {
        jdbcTemplate.update("INSERT INTO players (id, nickname, email, password_hash, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)", player.getId().toString(), player.getNickname().getValue(), player.getEmail().getValue(), player.getPasswordHash().getValue(), player.getCreatedAt(), player.getUpdatedAt());
        return player;
    }

    @Override
    public Player update(Player player) {
        int rows = jdbcTemplate.update("UPDATE players SET nickname = ?, email = ?, updated_at = ? WHERE id = ?", player.getNickname().getValue(), player.getEmail().getValue(), player.getUpdatedAt(), player.getId().toString());

        if (rows == 0) throw new ResourceNotFoundException("Player Not found");

        return player;
    }

    @Override
    public List<Player> findAll() {
        return jdbcTemplate.query(SELECT_PLAYER, playerRowMapper);
    }

    @Override
    public Optional<Player> findById(UUID id) {
        return  jdbcTemplate.query(SELECT_PLAYER + " WHERE id = ?", playerRowMapper, id.toString()).stream().findFirst();
    }

    @Override
    public Optional<Player> findByEmail(Email email) {
        return jdbcTemplate.query(SELECT_PLAYER + " WHERE email = ?", playerRowMapper, email.getValue()).stream().findFirst();
    }

    @Override
    public boolean emailExists(Email email) {
        Boolean exists = jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM players WHERE email = ?)", Boolean.class, email.getValue());

        return Boolean.TRUE.equals(exists);
    }

    @Override
    public boolean nicknameExists(Nickname nickname) {
        Boolean exists = jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM players WHERE nickname = ?)", Boolean.class, nickname.getValue());

        return Boolean.TRUE.equals(exists);
    }
}
