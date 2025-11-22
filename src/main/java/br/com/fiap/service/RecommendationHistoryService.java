package br.com.fiap.service;

import br.com.fiap.dao.RecommendationHistoryDao;
import br.com.fiap.dao.UserDao;
import br.com.fiap.dto.RecommendationHistoryRequestDto;
import br.com.fiap.dto.RecommendationHistoryResponseDto;
import br.com.fiap.model.RecommendationHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RecommendationHistoryService {

    @Inject
    RecommendationHistoryDao historyDao;

    @Inject
    UserDao userDao;

    public RecommendationHistoryResponseDto salvar(UUID userId, RecommendationHistoryRequestDto dto) {

        // valida se o usuário existe
        var user = userDao.findById(userId)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));

        RecommendationHistory h = new RecommendationHistory();
        h.setUserId(user.getId());
        h.setData(dto.getData());
        h.setMacroArea(dto.getMacro_area());
        h.setCursosRecomendados(dto.getCursos_recomendados());

        historyDao.insert(h);

        RecommendationHistoryResponseDto resp = new RecommendationHistoryResponseDto();
        resp.setId(h.getId().toString());
        resp.setData(h.getData());
        resp.setMacro_area(h.getMacroArea());
        resp.setCursos_recomendados(h.getCursosRecomendados());

        return resp;
    }

    public List<RecommendationHistoryResponseDto> listar(UUID userId) {

        var lista = historyDao.findByUserId(userId);

        return lista.stream().map(item -> {
            RecommendationHistoryResponseDto dto = new RecommendationHistoryResponseDto();
            dto.setId(item.getId().toString());
            dto.setData(item.getData());
            dto.setMacro_area(item.getMacroArea());
            dto.setCursos_recomendados(item.getCursosRecomendados());
            return dto;
        }).toList();
    }
}
