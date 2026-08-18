CREATE TABLE players (
    id VARCHAR(100) PRIMARY KEY,
    nickname VARCHAR(20),
    email VARCHAR(255),
    password_hash VARCHAR(255),
    role VARCHAR(20),
    player_rank VARCHAR(20),
    created_at DATETIME,
    updated_at DATETIME
);