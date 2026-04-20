package sv.edu.udb.usuarioservice.config.Exception.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApirErrorWraper {
    private List<ApiError> errores = new ArrayList<>();

    public void addError(ApiError error){
        errores.add(error);
    }

    public void addFieldError(String titulo, String descripcion, String fuente){
        ApiError error = ApiError.builder()
                .estatus(HttpStatus.BAD_REQUEST.value())
                .titulo(titulo)
                .descripcion(descripcion)
                .fuente(fuente)
                .build();

        errores.add(error);
    }
}
