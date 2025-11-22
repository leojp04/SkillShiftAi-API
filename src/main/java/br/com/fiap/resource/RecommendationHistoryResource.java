package br.com.fiap.resource;

import br.com.fiap.dto.RecommendationHistoryRequestDto;
import br.com.fiap.dto.RecommendationHistoryResponseDto;
import br.com.fiap.model.RecommendationHistory;
import br.com.fiap.service.JwtService;
import br.com.fiap.service.RecommendationHistoryService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/recomendacoes/historico")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecommendationHistoryResource {

    @Inject
    JwtService jwtService;

    @Inject
    RecommendationHistoryService historyService;

    @GET
    public Response listar(@HeaderParam("Authorization") String authorization) {
        String token = extractBearerToken(authorization);
        UUID userId = jwtService.validateAndGetUserId(token);

        List<RecommendationHistory> lista = historyService.listarPorUsuario(userId);

        List<RecommendationHistoryResponseDto> dtos = lista.stream()
                .map(RecommendationHistoryResponseDto::fromModel)
                .collect(Collectors.toList());

        return Response.ok(dtos).build();
    }

    @POST
    public Response criar(@HeaderParam("Authorization") String authorization,
                          @Valid RecommendationHistoryRequestDto request) {

        String token = extractBearerToken(authorization);
        UUID userId = jwtService.validateAndGetUserId(token);

        RecommendationHistory salvo = historyService.salvar(userId, request);

        return Response.status(Response.Status.CREATED)
                .entity(RecommendationHistoryResponseDto.fromModel(salvo))
                .build();
    }

    // -------------------------
    // MÉTODO CORRETO E LIMPO
    // -------------------------
    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new WebApplicationException("Token ausente ou inválido", Response.Status.UNAUTHORIZED);
        }
        return authorizationHeader.substring("Bearer ".length()).trim();
    }
}
