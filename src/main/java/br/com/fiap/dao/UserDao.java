package br.com.fiap.dao;

import br.com.fiap.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserDao {

    @Inject
    DataSource dataSource;

    public Optional<User> findByEmail(String email) {
        String sql = """
                SELECT ID, NOME, EMAIL, SENHA_HASH, CREATED_AT
                  FROM USERS
                 WHERE EMAIL = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por e-mail", e);
        }
    }

    public Optional<User> findById(UUID id) {
        String sql = """
                SELECT ID, NOME, EMAIL, SENHA_HASH, CREATED_AT
                  FROM USERS
                 WHERE ID = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por id", e);
        }
    }

    public void insert(User user) {
        String sql = """
                INSERT INTO USERS (ID, NOME, EMAIL, SENHA_HASH, CREATED_AT)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // garante que o ID existe
            if (user.getId() == null) {
                user.setId(UUID.randomUUID());
            }
            if (user.getCreatedAt() == null) {
                user.setCreatedAt(Instant.now());
            }

            stmt.setString(1, user.getId().toString());
            stmt.setString(2, user.getNome());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getSenhaHash());
            stmt.setTimestamp(5, Timestamp.from(user.getCreatedAt()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário", e);
        }
    }

    public void updatePassword(UUID userId, String novaSenhaHash) {
        String sql = """
                UPDATE USERS
                   SET SENHA_HASH = ?
                 WHERE ID = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novaSenhaHash);
            stmt.setString(2, userId.toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar senha do usuário", e);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(UUID.fromString(rs.getString("ID")));
        user.setNome(rs.getString("NOME"));
        user.setEmail(rs.getString("EMAIL"));
        user.setSenhaHash(rs.getString("SENHA_HASH"));

        Timestamp ts = rs.getTimestamp("CREATED_AT");
        if (ts != null) {
            user.setCreatedAt(ts.toInstant());
        }

        return user;
    }
}
