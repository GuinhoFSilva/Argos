package guinho.olympus.infrastructure.web.rest.auth.controller;

import guinho.olympus.core.application.usecase.player.LoginPlayerUseCase;
import guinho.olympus.core.application.usecase.player.RegisterPlayerUseCase;
import guinho.olympus.core.application.usecase.player.dto.CreatePlayerDto;
import guinho.olympus.core.application.usecase.player.dto.LoginInputDto;
import guinho.olympus.core.application.usecase.player.dto.LoginResponseDto;
import guinho.olympus.core.application.usecase.player.dto.ResponsePlayerDto;
import guinho.olympus.core.application.usecase.refresh_token.RefreshTokenUseCase;
import guinho.olympus.core.application.usecase.refresh_token.dto.RefreshTokenRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final LoginPlayerUseCase loginPlayerUseCase;
    private final RegisterPlayerUseCase registerPlayerUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthController(LoginPlayerUseCase loginPlayerUseCase, RegisterPlayerUseCase registerPlayerUseCase, RefreshTokenUseCase refreshTokenUseCase) {
        this.loginPlayerUseCase = loginPlayerUseCase;
        this.registerPlayerUseCase = registerPlayerUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponsePlayerDto> register(@RequestBody CreatePlayerDto request) {
        ResponsePlayerDto response = registerPlayerUseCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginInputDto request) {
        LoginResponseDto response = loginPlayerUseCase.execute(request);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody RefreshTokenRequest request) {
        LoginResponseDto response = refreshTokenUseCase.execute(request);

        return ResponseEntity.ok().body(response);
    }

}
