package br.com.fiap.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {

        // Erros de validação Bean Validation -> 400
        if (exception instanceof ConstraintViolationException cve) {
            StringBuilder sb = new StringBuilder();
            cve.getConstraintViolations()
                    .forEach(v -> sb.append(v.getMessage()).append("; "));

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponseDto(sb.toString()))
                    .build();
        }

        // Qualquer WebApplicationException -> usa o status que foi lançado (400, 401, 409, etc.)
        if (exception instanceof WebApplicationException wae) {
            int status = wae.getResponse().getStatus();
            String msg = wae.getMessage();
            return Response.status(status)
                    .entity(new ErrorResponseDto(msg))
                    .build();
        }

        // Fallback -> 500
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponseDto("Erro interno no servidor"))
                .build();
    }
}
