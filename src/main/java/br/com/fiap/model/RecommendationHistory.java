package br.com.fiap.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class RecommendationHistory {

    private UUID id;
    private UUID userId;
    private Instant data;
    private String macroArea;
    private List<String> cursosRecomendados;

    public RecommendationHistory() {}

    public RecommendationHistory(UUID id, UUID userId, Instant data, String macroArea, List<String> cursosRecomendados) {
        this.id = id;
        this.userId = userId;
        this.data = data;
        this.macroArea = macroArea;
        this.cursosRecomendados = cursosRecomendados;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Instant getData() {
        return data;
    }

    public void setData(Instant data) {
        this.data = data;
    }

    public String getMacroArea() {
        return macroArea;
    }

    public void setMacroArea(String macroArea) {
        this.macroArea = macroArea;
    }

    public List<String> getCursosRecomendados() {
        return cursosRecomendados;
    }

    public void setCursosRecomendados(List<String> cursosRecomendados) {
        this.cursosRecomendados = cursosRecomendados;
    }
    // getters e setters
}
