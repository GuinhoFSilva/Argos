package guinho.olympus.infrastructure.web.rest.player.controller;

import guinho.olympus.core.application.security.AuthenticatedPlayer;
import guinho.olympus.core.application.usecase.player.FindAllPlayersUseCase;
import guinho.olympus.core.application.usecase.player.FindPlayerByIdUseCase;
import guinho.olympus.core.application.usecase.player.PromotePlayetToAdminUseCase;
import guinho.olympus.core.application.usecase.player.dto.ResponsePlayerDto;
import guinho.olympus.core.domain.player.valueobject.Role;
import guinho.olympus.infrastructure.factory.AuthenticatedPlayerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/players")
public class PlayerController {
    private final FindPlayerByIdUseCase findPlayerByIdUseCase;
    private final FindAllPlayersUseCase findAllPlayersUseCase;
    private final PromotePlayetToAdminUseCase promotePlayetToAdminUseCase;
    private final AuthenticatedPlayerFactory authenticatedPlayerFactory;

    public PlayerController(FindPlayerByIdUseCase findPlayerByIdUseCase, FindAllPlayersUseCase findAllPlayersUseCase, PromotePlayetToAdminUseCase promotePlayetToAdminUseCase, AuthenticatedPlayerFactory authenticatedPlayerFactory) {
        this.findPlayerByIdUseCase = findPlayerByIdUseCase;
        this.findAllPlayersUseCase = findAllPlayersUseCase;
        this.promotePlayetToAdminUseCase = promotePlayetToAdminUseCase;
        this.authenticatedPlayerFactory = authenticatedPlayerFactory;
    }

    @GetMapping
    public ResponseEntity<List<ResponsePlayerDto>> findAll(JwtAuthenticationToken token) {
        AuthenticatedPlayer authenticatedPlayer = authenticatedPlayerFactory.from(token);
        List<ResponsePlayerDto> players = findAllPlayersUseCase.findAll(authenticatedPlayer);

        if (players.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(players);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePlayerDto> findById(@PathVariable UUID id, JwtAuthenticationToken token) {
        AuthenticatedPlayer authenticatedPlayer = authenticatedPlayerFactory.from(token);
        return ResponseEntity.ok().body(findPlayerByIdUseCase.findById(id, authenticatedPlayer));
    }

    @PatchMapping("/{id}/promote")
    public ResponseEntity<ResponsePlayerDto> makeAdmin(@PathVariable UUID id, JwtAuthenticationToken token) {
        AuthenticatedPlayer authenticatedPlayer = authenticatedPlayerFactory.from(token);
        return ResponseEntity.ok().body(promotePlayetToAdminUseCase.execute(id, authenticatedPlayer));
    }
}
