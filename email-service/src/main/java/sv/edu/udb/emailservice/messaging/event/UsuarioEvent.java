package sv.edu.udb.emailservice.messaging.event;

import lombok.Data;

@Data
public class UsuarioEvent {
    private String nombre;
    private String correo;
}
