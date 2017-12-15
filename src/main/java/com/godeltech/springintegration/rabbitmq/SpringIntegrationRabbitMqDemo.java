package com.godeltech.springintegration.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;

@Configuration
@EnableAutoConfiguration
@IntegrationComponentScan
public class SpringIntegrationRabbitMqDemo {

    static final String GATEWAY = "send.input";

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SpringIntegrationRabbitMqDemo.class, args);
        ctx.getBean(ProducerGateway.class).produce("Test");
        ctx.close();
    }

    @MessagingGateway
    public interface ProducerGateway {
        @Gateway(requestChannel = GATEWAY)
        void produce(String body);
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("bitnami");
        return connectionFactory;
    }

    @Bean
    Exchange worksExchange() {
        return ExchangeBuilder.topicExchange("work.exchange").build();
    }

    @Bean
    public Queue worksQueue() {
        return QueueBuilder.nonDurable("work.queue").build();
    }

    @Bean
    Binding worksBinding() {
        return BindingBuilder.bind(worksQueue()).to(worksExchange()).with("#").noargs();
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate r = new RabbitTemplate();
        r.setExchange("work.exchange");
        r.setRoutingKey("work");
        r.setConnectionFactory(rabbitConnectionFactory());
        return r;
    }

    @Bean
    public IntegrationFlow produce() {
        return f -> f.handle(Amqp.outboundAdapter(rabbitTemplate()));
    }

    @Bean
    public IntegrationFlow consume() {
        return IntegrationFlows.from(Amqp.inboundAdapter(rabbitConnectionFactory(), worksQueue()))
                .log("Message: ")
                .get();
    }
}
