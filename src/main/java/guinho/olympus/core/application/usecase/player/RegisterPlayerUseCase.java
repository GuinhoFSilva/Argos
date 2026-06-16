package guinho.olympus.core.application.usecase.player;

import guinho.olympus.core.application.abstractions.PasswordHasher;
import guinho.olympus.core.application.repository.player.PlayerMutation;
import guinho.olympus.core.application.repository.player.PlayerQuery;
import guinho.olympus.core.application.usecase.player.dto.CreatePlayerDto;
import guinho.olympus.core.application.usecase.player.dto.ResponsePlayerDto;
import guinho.olympus.core.application.usecase.player.mapper.PlayerMapper;
import guinho.olympus.core.application.usecase.player.shared.exception.EmailAlreadyExistsException;
import guinho.olympus.core.application.usecase.player.shared.exception.NicknameAlreadyExistsException;
import guinho.olympus.core.domain.player.Player;
import guinho.olympus.core.domain.player.valueobject.Email;
import guinho.olympus.core.domain.player.valueobject.Nickname;
import guinho.olympus.core.domain.player.valueobject.Password;
import guinho.olympus.core.domain.player.valueobject.PasswordHash;

public class RegisterPlayerUseCase {
    private final PlayerMutation mutationService;
    private final PlayerQuery queryService;
    private final PasswordHasher hasher;

    public RegisterPlayerUseCase(PlayerMutation mutationService, PlayerQuery queryService, PasswordHasher hasher) {
        this.mutationService = mutationService;
        this.queryService = queryService;
        this.hasher = hasher;
    }

    public ResponsePlayerDto execute(CreatePlayerDto command) {
        Password password = Password.of(command.password());
        Nickname nickname = Nickname.of(command.nickname());
        Email email = Email.of(command.email());
        boolean emailExists = queryService.emailExists(email);
        boolean nicknameExists = queryService.nicknameExists(nickname);

        if(emailExists) throw new EmailAlreadyExistsException("Email already exists.");

        if(nicknameExists) throw new NicknameAlreadyExistsException("Nickname already exists.");

        String passwordHash = hasher.hash(password.getValue());

        Player newPlayer = Player.create(
                nickname,
                email,
                PasswordHash.of(passwordHash)
        );

        Player savedPlayer = mutationService.save(newPlayer);

        return PlayerMapper.toResponse(savedPlayer);
    }
}
