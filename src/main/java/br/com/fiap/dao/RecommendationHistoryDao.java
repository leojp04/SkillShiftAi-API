package br.com.fiap.dao;

import br.com.fiap.model.RecommendationHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RecommendationHistoryDao {

    @Inject
    DataSource dataSource;

    public void insert(RecommendationHistory history) {
        String sql = """
                INSERT INTO RECOMMENDATION_HISTORY
                    (ID, USER_ID, DATA, MACRO_AREA, CURSOS_RECOMENDADOS)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (history.getId() == null) {
                history.setId(UUID.randomUUID());
            }

            stmt.setString(1, history.getId().toString());
            stmt.setString(2, history.getUserId().toString());
            stmt.setTimestamp(3, Timestamp.from(history.getData()));
            stmt.setString(4, history.getMacroArea());
            // aqui esperamos que cursosRecomendados já tenha sido convertido para JSON
            stmt.setString(5, String.join(";", history.getCursosRecomendados()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir histórico de recomendações", e);
        }
    }

    public List<RecommendationHistory> findByUserId(UUID userId) {
        String sql = """
                SELECT ID, USER_ID, DATA, MACRO_AREA, CURSOS_RECOMENDADOS
                  FROM RECOMMENDATION_HISTORY
                 WHERE USER_ID = ?
                 ORDER BY DATA DESC
                """;

        List<RecommendationHistory> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToHistory(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar histórico de recomendações", e);
        }

        return lista;
    }

    private RecommendationHistory mapResultSetToHistory(ResultSet rs) throws SQLException {
        RecommendationHistory h = new RecommendationHistory();

        h.setId(UUID.fromString(rs.getString("ID")));
        h.setUserId(UUID.fromString(rs.getString("USER_ID")));

        Timestamp ts = rs.getTimestamp("DATA");
        if (ts != null) {
            h.setData(ts.toInstant());
        }

        h.setMacroArea(rs.getString("MACRO_AREA"));

        String cursosStr = rs.getString("CURSOS_RECOMENDADOS");
        if (cursosStr != null && !cursosStr.isBlank()) {

            List<String> cursos = List.of(cursosStr.split(";"));
            h.setCursosRecomendados(cursos);
        }

        return h;
    }
}
