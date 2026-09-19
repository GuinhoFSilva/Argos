CREATE TABLE players (
    id VARCHAR(100) PRIMARY KEY,
    nickname VARCHAR(20),
    email VARCHAR(255),
    password_hash VARCHAR(255),
    role VARCHAR(20),
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE refresh_tokens (
    id VARCHAR(100) PRIMARY KEY,
    player_id VARCHAR(100),
    token_hash VARCHAR(255),
    expires_at DATETIME,
    revoked TINYINT(1),
    FOREIGN KEY(player_id) REFERENCES players(id)
);