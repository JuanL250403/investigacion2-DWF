package sv.edu.udb.usuarioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv.edu.udb.usuarioservice.repository.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsUsuarioByCorreo(String correo);

}
