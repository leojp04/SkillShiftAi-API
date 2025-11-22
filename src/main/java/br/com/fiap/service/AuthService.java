package br.com.fiap.service;

import br.com.fiap.dao.UserDao;
import br.com.fiap.dto.AuthResponseDto;
import br.com.fiap.dto.LoginRequestDto;
import br.com.fiap.dto.RegisterRequestDto;
import br.com.fiap.dto.SimpleUserDto;
import br.com.fiap.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AuthService {

    @Inject
    UserDao userDao;

    @Inject
    JwtService jwtService;

    public AuthResponseDto register(RegisterRequestDto dto) {
        // UserDao.findByEmail retorna Optional<User>
        Optional<User> existingOpt = userDao.findByEmail(dto.getEmail());
        if (existingOpt.isPresent()) {
            throw new WebApplicationException(
                    "E-mail já cadastrado",
                    Response.Status.CONFLICT
            );
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        // ideal: hash de senha; aqui mantém simples
        user.setSenhaHash(dto.getSenha());
        user.setCreatedAt(Instant.now());

        userDao.insert(user);

        String token = jwtService.generateToken(user);

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(token);
        response.setUsuario(toSimpleUserDto(user));

        return response;
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        Optional<User> userOpt = userDao.findByEmail(dto.getEmail());

        User user = userOpt.orElseThrow(() ->
                new WebApplicationException("Credenciais inválidas", Response.Status.BAD_REQUEST)
        );

        if (!user.getSenhaHash().equals(dto.getSenha())) {
            throw new WebApplicationException("Credenciais inválidas", Response.Status.BAD_REQUEST);
        }

        String token = jwtService.generateToken(user);

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(token);
        response.setUsuario(toSimpleUserDto(user));

        return response;
    }

    private SimpleUserDto toSimpleUserDto(User user) {
        SimpleUserDto dto = new SimpleUserDto();
        dto.setId(user.getId().toString());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
