package br.com.fiap.dto;

import br.com.fiap.model.RecommendationHistory;
import java.time.Instant;
import java.util.List;

public class RecommendationHistoryResponseDto {

    private String id;
    private Instant data;
    private String macro_area;
    private List<String> cursos_recomendados;

    public static RecommendationHistoryResponseDto fromModel(RecommendationHistory model) {
        RecommendationHistoryResponseDto dto = new RecommendationHistoryResponseDto();
        dto.setId(model.getId().toString());
        dto.setData(model.getData());
        dto.setMacro_area(model.getMacroArea());
        dto.setCursos_recomendados(model.getCursosRecomendados());
        return dto;
    }

    // Getters e Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getData() {
        return data;
    }

    public void setData(Instant data) {
        this.data = data;
    }

    public String getMacro_area() {
        return macro_area;
    }

    public void setMacro_area(String macro_area) {
        this.macro_area = macro_area;
    }

    public List<String> getCursos_recomendados() {
        return cursos_recomendados;
    }

    public void setCursos_recomendados(List<String> cursos_recomendados) {
        this.cursos_recomendados = cursos_recomendados;
    }
}
