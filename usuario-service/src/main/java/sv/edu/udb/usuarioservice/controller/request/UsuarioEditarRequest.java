package sv.edu.udb.usuarioservice.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioEditarRequest {
    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @PastOrPresent(message = "La fecha nacimiento debe ser inferior a la actual")
    private LocalDate fechaNacimiento;
}
