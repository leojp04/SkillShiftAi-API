package br.com.fiap.service;

import br.com.fiap.dao.UserDao;
import br.com.fiap.dto.*;
import br.com.fiap.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.WebApplicationException;

import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class AuthService {

    @Inject
    UserDao userDao;

    // Token "fake" por enquanto (vai virar JWT depois)
    private static final String TOKEN_PREFIX = "fake-token-";

    public String gerarTokenFake(UUID userId) {
        return TOKEN_PREFIX + userId;
    }

    public UUID extrairUserIdDeToken(String token) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            throw new NotAuthorizedException("Token inválido");
        }
        String idStr = token.substring(TOKEN_PREFIX.length());
        return UUID.fromString(idStr);
    }

    public AuthResponseDto registrar(RegisterRequestDto dto) {

        var existente = userDao.findByEmail(dto.getEmail());
        if (existente.isPresent()) {
            // 409 – conflito (e-mail já existe)
            throw new WebApplicationException("E-mail já existe", 409);
        }

        User u = new User();
        u.setId(UUID.randomUUID());
        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        // ⚠️ Por enquanto sem hash, depois trocamos pra BCrypt
        u.setSenhaHash(dto.getSenha());
        u.setCreatedAt(Instant.now());

        userDao.insert(u);

        SimpleUserDto usuDto = new SimpleUserDto();
        usuDto.setId(u.getId().toString());
        usuDto.setNome(u.getNome());
        usuDto.setEmail(u.getEmail());

        AuthResponseDto resp = new AuthResponseDto();
        resp.setToken(gerarTokenFake(u.getId()));
        resp.setUsuario(usuDto);

        return resp;
    }

    public AuthResponseDto login(LoginRequestDto dto) {

        var user = userDao.findByEmail(dto.getEmail())
                .orElseThrow(() -> new WebApplicationException("Credenciais inválidas", 400));

        // ⚠️ Por enquanto sem hash
        if (!user.getSenhaHash().equals(dto.getSenha())) {
            throw new WebApplicationException("Credenciais inválidas", 400);
        }

        SimpleUserDto usuDto = new SimpleUserDto();
        usuDto.setId(user.getId().toString());
        usuDto.setNome(user.getNome());
        usuDto.setEmail(user.getEmail());

        AuthResponseDto resp = new AuthResponseDto();
        resp.setToken(gerarTokenFake(user.getId()));
        resp.setUsuario(usuDto);

        return resp;
    }

    public MeResponseDto buscarMe(UUID userId) {
        var user = userDao.findById(userId)
                .orElseThrow(() -> new NotAuthorizedException("Usuário não encontrado"));

        MeResponseDto dto = new MeResponseDto();
        dto.setId(user.getId().toString());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public void alterarSenha(UUID userId, ChangePasswordRequestDto dto) {

        var user = userDao.findById(userId)
                .orElseThrow(() -> new NotAuthorizedException("Usuário não encontrado"));

        if (!user.getSenhaHash().equals(dto.getSenhaAtual())) {
            // 400 senhaAtual incorreta
            throw new WebApplicationException("Senha atual incorreta", 400);
        }

        userDao.updatePassword(userId, dto.getNovaSenha());
    }
}
