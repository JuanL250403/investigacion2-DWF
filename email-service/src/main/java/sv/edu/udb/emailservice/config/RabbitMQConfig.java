package sv.edu.udb.emailservice.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sv.edu.udb.emailservice.messaging.constants.RabbitMQConstants;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE= RabbitMQConstants.EXCHANGE;
    public static final String ROUTING_USUARIO= "usuario_key";
    public static final String QUEUE_USUARIO= "usuario_queue";


    @Bean
    public Queue queueUsuario(){
        return new Queue(QUEUE_USUARIO);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding bindingUsuario(Queue queueUsuario, TopicExchange topicExchange){
        return BindingBuilder.bind(queueUsuario).to(topicExchange).with("usuario.*");
    }

    @Bean
    public MessageConverter messageConverter(){
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        final var template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return  template;
    }
}
