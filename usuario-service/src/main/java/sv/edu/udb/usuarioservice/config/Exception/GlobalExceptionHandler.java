package sv.edu.udb.usuarioservice.config.Exception;

import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sv.edu.udb.usuarioservice.Exception.UsuarioExistenteException;
import sv.edu.udb.usuarioservice.config.Exception.dto.ApiError;
import sv.edu.udb.usuarioservice.config.Exception.dto.ApirErrorWraper;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApirErrorWraper error = procesarErrores(ex.getBindingResult().getAllErrors());

        return handleExceptionInternal(ex, error, headers, status, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpStatusCode statusCode, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), statusCode, request);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (Objects.isNull(body)) {

            ApirErrorWraper error = procesarError(ex.getClass().getSimpleName(), ex.getMessage(), "base", statusCode);

            return new ResponseEntity<>(error, headers, statusCode);
        } else {
            return new ResponseEntity<>(body, headers, statusCode);
        }
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAcces(DataAccessException ex, WebRequest request){
        return handleExceptionInternal(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handlerIllegalArgumentation(IllegalArgumentException ex, WebRequest request){
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handlerEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, HttpStatus.NOT_FOUND, request);
    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handler(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(UsuarioExistenteException.class)
    public ResponseEntity<Object> hanldeUsuarioExistente(UsuarioExistenteException ex, WebRequest request){
        return  handleExceptionInternal(ex, HttpStatus.CONFLICT, request);
    }
    private ApirErrorWraper procesarError(String titulo, String descripcion, String fuente, HttpStatusCode estatus) {

        ApiError apiError = ApiError.builder()
                .estatus(estatus.value())
                .titulo(titulo)
                .descripcion(descripcion)
                .fuente(fuente)
                .build();

        ApirErrorWraper errorWraper = new ApirErrorWraper();

        errorWraper.addError(apiError);

        return errorWraper;
    }

    private ApirErrorWraper procesarErrores(List<ObjectError> errores) {
        ApirErrorWraper errorWraper = new ApirErrorWraper();

        for (ObjectError error : errores) {
            String titulo;
            String descripcion;
            if (error instanceof FieldError) {
                final FieldError fieldError = (FieldError) error;
                String field = fieldError.getField();
                titulo = error.getClass().getSimpleName();
                descripcion = error.getDefaultMessage();

                errorWraper.addFieldError(titulo, descripcion, field);
            } else {
                titulo = error.getClass().getSimpleName();
                descripcion = error.getDefaultMessage();
                errorWraper.addFieldError(titulo, descripcion, "base");
            }
        }

        return errorWraper;
    }

}
