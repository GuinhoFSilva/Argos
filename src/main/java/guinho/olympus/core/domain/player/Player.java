package guinho.olympus.core.domain.player;

import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;

import java.time.LocalDateTime;
import java.util.UUID;

public class Player {
    private UUID id;
    private Nickname nickname;
    private Email email;
    private PasswordHash passwordHash;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Player(UUID id, Nickname nickname, Email email, PasswordHash passwordHash, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Player create( Nickname nickname, Email email, PasswordHash passwordHash) {
        return new Player(UUID.randomUUID(), nickname, email, passwordHash, LocalDateTime.now(), LocalDateTime.now());
    }

    public static Player reconstitute(UUID id, Nickname nickname, Email email, PasswordHash passwordHash, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Player(id, nickname, email, passwordHash, createdAt, updatedAt);
    }

    public void changeEmail(Email email) {
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public void renameTo(Nickname nickname){
        this.nickname = nickname;
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public Email getEmail() {
        return email;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", nickname=" + nickname +
                ", email=" + email +
                ", passwordHash=" + passwordHash +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
