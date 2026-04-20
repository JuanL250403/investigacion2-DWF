package sv.edu.udb.usuarioservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.usuarioservice.controller.request.UsuarioEditarRequest;
import sv.edu.udb.usuarioservice.controller.request.UsuarioRequest;
import sv.edu.udb.usuarioservice.controller.response.UsuarioResponse;
import sv.edu.udb.usuarioservice.repository.domain.Usuario;
import sv.edu.udb.usuarioservice.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponse> obtenerUsuario() {
        return usuarioService.obtenerUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorNombre(@PathVariable("id") Long id) {
        return usuarioService.obtnerUsuario(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crear(@Valid @RequestBody UsuarioRequest usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse editar(@PathVariable("id") Long id, @Valid @RequestBody UsuarioEditarRequest usuario) {
        return usuarioService.editarUsuario(id, usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        usuarioService.eliminarUsuario(id);
    }
}
