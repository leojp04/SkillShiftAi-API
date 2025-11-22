package br.com.fiap.resource;

import br.com.fiap.dto.AuthResponseDto;
import br.com.fiap.dto.LoginRequestDto;
import br.com.fiap.dto.RegisterRequestDto;
import br.com.fiap.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/register")
    public AuthResponseDto register(@Valid RegisterRequestDto dto) {
        // 200 com { token, usuario } ou erros 400/409
        return authService.register(dto);
    }

    @POST
    @Path("/login")
    public AuthResponseDto login(@Valid LoginRequestDto dto) {
        // 200 com { token, usuario } ou erro 400
        return authService.login(dto);
    }
}
