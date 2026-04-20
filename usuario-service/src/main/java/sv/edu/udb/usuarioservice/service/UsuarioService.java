package sv.edu.udb.usuarioservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sv.edu.udb.usuarioservice.Exception.UsuarioExistenteException;
import sv.edu.udb.usuarioservice.controller.request.UsuarioEditarRequest;
import sv.edu.udb.usuarioservice.controller.request.UsuarioRequest;
import sv.edu.udb.usuarioservice.controller.response.UsuarioResponse;
import sv.edu.udb.usuarioservice.messaging.constants.RabbitMQConstants;
import sv.edu.udb.usuarioservice.messaging.event.UsuarioEvent;
import sv.edu.udb.usuarioservice.messaging.producer.UsuarioEventPublisher;
import sv.edu.udb.usuarioservice.repository.UsuarioRepository;
import sv.edu.udb.usuarioservice.repository.domain.Usuario;
import sv.edu.udb.usuarioservice.service.mapper.UsuarioMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    private final UsuarioEventPublisher usuarioEventPublisher;

    public List<UsuarioResponse> obtenerUsuarios() {
        final List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarioMapper.toUsuarioListResponse(usuarios);
    }

    public UsuarioResponse obtnerUsuario(Long id) {
        final Usuario usuarioCreado = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no existente"));

        return usuarioMapper.toUsuarioResponse(usuarioCreado);
    }

    public UsuarioResponse crearUsuario(UsuarioRequest usuario) {

        if(usuarioRepository.existsUsuarioByCorreo(usuario.getCorreo())){
            throw new UsuarioExistenteException("Ya existe una cuenta registrada con el correo " + usuario.getCorreo());
        }

        final Usuario usuarioCrear = usuarioMapper.toUsuario(usuario);

        final Usuario usuarioCreado = usuarioRepository.save(usuarioCrear);

        final UsuarioEvent usuarioCreadoEvnt = new UsuarioEvent();

        usuarioCreadoEvnt.setNombre(usuarioCreado.getNombre());
        usuarioCreadoEvnt.setCorreo(usuarioCreado.getCorreo());
        usuarioEventPublisher.publicarUsuarioCreado(usuarioCreadoEvnt);
        return usuarioMapper.toUsuarioResponse(usuarioCreado);
    }

    public UsuarioResponse editarUsuario(Long id, UsuarioEditarRequest usuario) {
        final Usuario usuarioEditar = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        usuarioEditar.setNombre(usuario.getNombre());
        usuarioEditar.setCorreo(usuarioEditar.getCorreo());
        usuarioEditar.setFechaNacimiento(usuarioEditar.getFechaNacimiento());

        final Usuario usuarioEditado = usuarioRepository.save(usuarioEditar);

        final UsuarioEvent usuarioEditadoEvnt = usuarioMapper.toUsuarioEvent(usuarioEditado);

        usuarioEditadoEvnt.setNombre(usuarioEditado.getCorreo());
        usuarioEditadoEvnt.setCorreo(usuarioEditado.getCorreo());
        usuarioEventPublisher.publicarUsuarioEditado(usuarioEditadoEvnt);
        return usuarioMapper.toUsuarioResponse(usuarioEditado);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no existente"));

        usuarioRepository.deleteById(id);
    }
}
