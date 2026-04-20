package sv.edu.udb.usuarioservice.config.Exception.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {
    private int estatus;

    private String fuente;

    private String titulo;

    private String descripcion;
}
