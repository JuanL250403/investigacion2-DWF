package sv.edu.udb.emailservice.messaging.consumer;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import sv.edu.udb.emailservice.config.RabbitMQConfig;
import sv.edu.udb.emailservice.messaging.constants.RabbitMQConstants;
import sv.edu.udb.emailservice.messaging.event.UsuarioEvent;
import sv.edu.udb.emailservice.repository.Usuario;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_USUARIO)
    public void creacionUsuario(UsuarioEvent usuario, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        if (RabbitMQConstants.ROUTING_USUARIO_CREADO.equals(routingKey)) {

            final String asunto = "Su usuario fue creado exitosamente";
            final String contenido = """
                    <h1>Bienvenido</h1>
                    <p>Se ha registrado exitosamente a nuestra plataforma</p>
                    """;

            enviarCorreo(usuario.getCorreo(), asunto, contenido);
        }

    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_USUARIO)
    public void edicionUsuario(UsuarioEvent usuario, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {

        if (RabbitMQConstants.ROUTING_USUARIO_EDITADO.equals(routingKey)) {
            final String asunto = "Se han actualizado sus datos";
            final String contenido = """
                    <h1>Cambios realizados</h1>
                    <p>Hemos modificado los datos de su cuenta exitosamente</p>
                    """;

            enviarCorreo(usuario.getCorreo(), asunto, contenido);
        }
    }

    private void enviarCorreo(String destinatario, String asunto, String contenido) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenido, true);

            mailSender.send(mensaje);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
