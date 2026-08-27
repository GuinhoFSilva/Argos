package guinho.olympus.core.application.usecase.player.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResponsePlayerDto(UUID id, String nickname, String email, String role, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
