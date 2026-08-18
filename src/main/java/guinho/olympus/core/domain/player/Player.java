package guinho.olympus.core.domain.player;

import guinho.olympus.core.domain.player.enums.Rank;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;
import guinho.olympus.core.domain.player.valueobject.Role;
import guinho.olympus.core.domain.shared.UnchangedFieldException;

import java.time.LocalDateTime;
import java.util.UUID;

public class Player {
    private final UUID id;
    private Nickname nickname;
    private Email email;
    private Role role;
    private Rank rank;
    private PasswordHash passwordHash;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Player(UUID id, Nickname nickname, Email email, PasswordHash passwordHash, Role role, Rank rank, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.rank = rank;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Player create(Nickname nickname, Email email, Rank rank, PasswordHash passwordHash, Role role) {
        return new Player(UUID.randomUUID(), nickname, email, passwordHash, role, rank, LocalDateTime.now(), LocalDateTime.now());
    }

    public static Player reconstitute(UUID id, Nickname nickname, Email email, PasswordHash passwordHash, Role role, Rank rank, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Player(id, nickname, email, passwordHash, role, rank, createdAt, updatedAt);
    }

    public void changeEmail(Email email) {
        if (email.getValue().equalsIgnoreCase(this.email.getValue())) {
            throw new UnchangedFieldException("The new email must be different from the current email");
        }
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public void renameTo(Nickname nickname) {
        if (nickname.getValue().equalsIgnoreCase(this.nickname.getValue())) {
            throw new UnchangedFieldException("The new nickname must be different from the current nickname");
        }
        this.nickname = nickname;
        this.updatedAt = LocalDateTime.now();
    }

    public void promoteToAdmin(Role role) {
        if (role.getValue().equalsIgnoreCase(this.role.getValue())) {
            throw new UnchangedFieldException("This user is already an admin");
        }

        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public Rank getRank() {
        return rank;
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
                ", role=" + role +
                ", passwordHash=" + passwordHash +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
