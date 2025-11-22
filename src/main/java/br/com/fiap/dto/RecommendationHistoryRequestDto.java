package br.com.fiap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public class RecommendationHistoryRequestDto {

    @NotNull(message = "A data é obrigatória")
    private Instant data;

    @NotBlank(message = "A macro_area é obrigatória")
    private String macro_area;

    @NotNull(message = "A lista de cursos_recomendados é obrigatória")
    private List<String> cursos_recomendados;

    // getters e setters


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
