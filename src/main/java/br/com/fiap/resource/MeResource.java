package br.com.fiap.resource;

import br.com.fiap.dto.ChangePasswordRequestDto;
import br.com.fiap.dto.MeResponseDto;
import br.com.fiap.dto.MensagemResponseDto;
import br.com.fiap.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("/me")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MeResource {

    @Inject
    AuthService authService;

    @GET
    public MeResponseDto me(@Context HttpHeaders headers) {
        UUID userId = extrairUserId(headers);
        return authService.buscarMe(userId);
    }

    @PATCH
    @Path("/password")
    public MensagemResponseDto changePassword(@Valid ChangePasswordRequestDto dto,
                                              @Context HttpHeaders headers) {
        UUID userId = extrairUserId(headers);
        authService.alterarSenha(userId, dto);
        return new MensagemResponseDto("Senha atualizada com sucesso.");
    }

    private UUID extrairUserId(HttpHeaders headers) {
        String auth = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Token não informado");
        }
        String token = auth.substring("Bearer ".length()).trim();
        return authService.extrairUserIdDeToken(token);
    }
}
