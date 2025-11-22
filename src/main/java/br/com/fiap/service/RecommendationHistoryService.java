package br.com.fiap.service;

import br.com.fiap.dao.RecommendationHistoryDao;
import br.com.fiap.dto.RecommendationHistoryRequestDto;
import br.com.fiap.model.RecommendationHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RecommendationHistoryService {

    @Inject
    RecommendationHistoryDao historyDao;

    public List<RecommendationHistory> listarPorUsuario(UUID userId) {
        return historyDao.findByUserId(userId);
    }

    public RecommendationHistory salvar(UUID userId, RecommendationHistoryRequestDto request) {
        RecommendationHistory history = new RecommendationHistory();
        history.setId(UUID.randomUUID());
        history.setUserId(userId);

        // ✅ aqui usamos exatamente os getters que existem no DTO
        history.setData(request.getData());
        history.setMacroArea(request.getMacro_area());
        history.setCursosRecomendados(request.getCursos_recomendados());

        historyDao.insert(history);

        return history;
    }
}
