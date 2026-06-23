package guinho.olympus.infrastructure.web.rest.player.controller;

import guinho.olympus.core.application.usecase.player.FindAllPlayersUseCase;
import guinho.olympus.core.application.usecase.player.FindPlayerByIdUseCase;
import guinho.olympus.core.application.usecase.player.dto.ResponsePlayerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/players")
public class PlayerController {
    private final FindPlayerByIdUseCase findPlayerByIdUseCase;
    private final FindAllPlayersUseCase findAllPlayersUseCase;

    public PlayerController(FindPlayerByIdUseCase findPlayerByIdUseCase, FindAllPlayersUseCase findAllPlayersUseCase) {
        this.findPlayerByIdUseCase = findPlayerByIdUseCase;
        this.findAllPlayersUseCase = findAllPlayersUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ResponsePlayerDto>> findAll() {
        List<ResponsePlayerDto> players = findAllPlayersUseCase.findAll();

        if(players.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(players);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePlayerDto> findById(@PathVariable String id) {
        return ResponseEntity.ok().body(findPlayerByIdUseCase.findById(UUID.fromString(id)));
    }
}
