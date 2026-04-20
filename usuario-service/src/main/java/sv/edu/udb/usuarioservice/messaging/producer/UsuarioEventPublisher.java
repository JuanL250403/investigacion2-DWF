package sv.edu.udb.usuarioservice.messaging.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sv.edu.udb.usuarioservice.messaging.constants.RabbitMQConstants;
import sv.edu.udb.usuarioservice.messaging.event.UsuarioEvent;

@Service
@RequiredArgsConstructor
public class UsuarioEventPublisher {
    private final RabbitTemplate template;

    public void publicarUsuarioCreado(UsuarioEvent usuario) {
        template.convertAndSend(RabbitMQConstants.EXCHANGE,
                RabbitMQConstants.ROUTING_USUARIO_CREADO,
                usuario);
    }

    public void publicarUsuarioEditado(UsuarioEvent usuario) {
        template.convertAndSend(RabbitMQConstants.EXCHANGE,
                RabbitMQConstants.ROUTING_USUARIO_EDITADO,
                usuario);
    }
}
