package br.com.fiap.resource;

import br.com.fiap.dao.UserDao;
import br.com.fiap.dto.MeResponseDto;
import br.com.fiap.dto.MensagemResponseDto;
import br.com.fiap.model.User;
import br.com.fiap.service.JwtService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;
import java.util.UUID;

@Path("/me")
@Produces(MediaType.APPLICATION_JSON)
public class MeResource {

    @Inject
    JwtService jwtService;

    @Inject
    UserDao userDao;

    @GET
    public Response me(@HeaderParam("Authorization") String authorization) {
        String token = extractBearerToken(authorization);
        UUID userId = jwtService.validateAndGetUserId(token);

        Optional<User> userOpt = userDao.findById(userId);
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new MensagemResponseDto("Usuário não encontrado"))
                    .build();
        }

        User user = userOpt.get();

        MeResponseDto dto = new MeResponseDto();
        dto.setId(user.getId().toString()); // <- String
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());

        return Response.ok(dto).build();
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new WebApplicationException("Token ausente ou inválido", Response.Status.UNAUTHORIZED);
        }
        return authorizationHeader.substring("Bearer ".length()).trim();
    }
}
