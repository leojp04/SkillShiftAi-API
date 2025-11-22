package br.com.fiap.service;

import br.com.fiap.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@ApplicationScoped
public class JwtService {

    private final Algorithm algorithm;
    private final String issuer;
    private final long expirationSeconds;

    public JwtService(
            @ConfigProperty(name = "app.jwt.secret") String secret,
            @ConfigProperty(name = "app.jwt.issuer") String issuer,
            @ConfigProperty(name = "app.jwt.expiration-seconds") long expirationSeconds
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.issuer = issuer;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(User user) {
        Instant now = Instant.now();

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getId().toString())      // ID do usuário
                .withClaim("nome", user.getNome())
                .withClaim("email", user.getEmail())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(expirationSeconds)))
                .sign(algorithm);
    }

    public UUID validateAndGetUserId(String token) {
        try {
            DecodedJWT decoded = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);

            String subject = decoded.getSubject();
            return UUID.fromString(subject);
        } catch (JWTVerificationException | IllegalArgumentException e) {
            throw new WebApplicationException(
                    "Token inválido ou expirado",
                    Response.Status.UNAUTHORIZED
            );
        }
    }
}
