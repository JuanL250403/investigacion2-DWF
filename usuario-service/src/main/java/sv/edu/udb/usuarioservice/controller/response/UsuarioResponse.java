package sv.edu.udb.usuarioservice.controller.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioResponse {
    private Long id;

    private String nombre;

    private String correo;

    private LocalDate fechaNacimiento;
}
