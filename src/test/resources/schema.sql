CREATE TABLE players (
    id VARCHAR(100) PRIMARY KEY,
    nickname VARCHAR(20),
    email VARCHAR(255),
    password_hash VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);