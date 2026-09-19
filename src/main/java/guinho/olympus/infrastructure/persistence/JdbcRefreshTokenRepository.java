package guinho.olympus.infrastructure.persistence;

import guinho.olympus.core.application.repository.refresh_token.RefreshTokenMutation;
import guinho.olympus.core.application.repository.refresh_token.RefreshTokenQuery;
import guinho.olympus.core.domain.refresh_token.RefreshToken;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcRefreshTokenRepository implements RefreshTokenMutation, RefreshTokenQuery {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_REFRESH_TOKEN = "SELECT id, player_id, token, expires_at, revoked FROM refresh_tokens";


    public JdbcRefreshTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<RefreshToken> refreshTokenRowMapper = ((rs, rowNum) -> RefreshToken.reconstitute(
            UUID.fromString(rs.getString("id")),
            UUID.fromString(rs.getString("player_id")),
            (rs.getString("token")),
            rs.getTimestamp("expires_at").toLocalDateTime(),
            rs.getBoolean("revoked")
    ));

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        jdbcTemplate.update("INSERT INTO refresh_tokens (id, player_id, token, expires_at, revoked) VALUES (?, ?, ?, ?, ?)", refreshToken.getId().toString(), refreshToken.getPlayerId().toString(), refreshToken.getToken(), refreshToken.getExpiresAt(), refreshToken.isRevoked());

        return refreshToken;
    }

    @Override
    public void revoke(RefreshToken refreshToken) {
        jdbcTemplate.update("UPDATE refresh_tokens SET revoked = 1 WHERE id = ?", refreshToken.getId().toString());
    }

    @Override
    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return jdbcTemplate.query(SELECT_REFRESH_TOKEN + " WHERE token = ?", refreshTokenRowMapper, refreshToken).stream().findFirst();
    }
}
