package sv.edu.udb.usuarioservice.service.mapper;

import org.mapstruct.Mapper;
import sv.edu.udb.usuarioservice.controller.request.UsuarioEditarRequest;
import sv.edu.udb.usuarioservice.controller.request.UsuarioRequest;
import sv.edu.udb.usuarioservice.controller.response.UsuarioResponse;
import sv.edu.udb.usuarioservice.messaging.event.UsuarioEvent;
import sv.edu.udb.usuarioservice.repository.domain.Usuario;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    List<UsuarioResponse> toUsuarioListResponse(final List<Usuario> usuarios);

    UsuarioResponse toUsuarioResponse(final Usuario usuario);

    Usuario toUsuario(UsuarioRequest usuarioRequest);

    UsuarioEvent toUsuarioEvent(Usuario usuario);
}
