package br.com.fiap.resource;

import br.com.fiap.dto.RecommendationHistoryRequestDto;
import br.com.fiap.dto.RecommendationHistoryResponseDto;
import br.com.fiap.service.AuthService;
import br.com.fiap.service.RecommendationHistoryService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/recomendacoes/historico")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RecommendationHistoryResource {

    @Inject
    RecommendationHistoryService historyService;

    @Inject
    AuthService authService;

    @GET
    public List<RecommendationHistoryResponseDto> listar(@Context HttpHeaders headers) {
        UUID userId = extrairUserId(headers);
        return historyService.listar(userId);
    }

    @POST
    public Response criar(@Valid RecommendationHistoryRequestDto dto,
                          @Context HttpHeaders headers) {
        UUID userId = extrairUserId(headers);
        RecommendationHistoryResponseDto resp = historyService.salvar(userId, dto);
        // 201 com o objeto criado
        return Response.status(Response.Status.CREATED).entity(resp).build();
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
