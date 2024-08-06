package microservices.book.gamification.configuration;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
public class AMQPConfiguration {

  @Bean
  public TopicExchange challengesTopicExchange(
      @Value("${amqp.exchange.attempts}") final String exchangeName) {

    return ExchangeBuilder
        .topicExchange(exchangeName)
        .durable(true)
        .build();
  }

  @Bean
  public Queue gamificationQueue(
      @Value("${amqp.queue.gamification}") final String queueName) {
    return QueueBuilder
        .durable(queueName)
        .build();
  }

  @Bean
  public Binding correctAttemptsBinding(final Queue gamificationQueue,
      final TopicExchange attemptsExchange) {
    return BindingBuilder.bind(gamificationQueue)
        .to(attemptsExchange)
        .with("attempt.correct");
  }

  @Bean
  public MessageHandlerMethodFactory messageHandlerMethodFactory() {
    DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
    final MappingJackson2MessageConverter jsonConverter = new MappingJackson2MessageConverter();
    //  add the ParameterNamesModule to avoid having to use empty constructors for your event classes
    jsonConverter.getObjectMapper().registerModule(new ParameterNamesModule(Mode.PROPERTIES));
    factory.setMessageConverter(jsonConverter);

    return factory;
  }

  // To configure the listeners to use a JSON deserialization, you have to override
  // the RabbitListenerConfigurer bean with an implementation that uses the custom
  // MessageHandlerMethodFactory.
  @Bean
  public RabbitListenerConfigurer rabbitListenerConfigurer(final MessageHandlerMethodFactory messageHandlerMethodFactory) {
    return (c) -> c.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
  }
}
