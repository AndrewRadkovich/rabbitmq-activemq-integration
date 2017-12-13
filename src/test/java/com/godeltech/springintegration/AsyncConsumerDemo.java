package com.godeltech.springintegration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@DirtiesContext
public class AsyncConsumerDemo {

    static final String GATEWAY = "produce";

    @Autowired
    MyConfiguration.ProducerGateway gateway;

    @Test
    public void demo() throws Exception {
        gateway.produce("Test");
    }

    @Configuration
    @EnableIntegration
    public static class MyConfiguration {

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
            return BindingBuilder
                    .bind(worksQueue())
                    .to(worksExchange()).with("#").noargs();
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
            return IntegrationFlows.from(GATEWAY)
                    .handle(Amqp.outboundAdapter(rabbitTemplate()))
                    .get();
        }

        @Bean
        public IntegrationFlow consume() {
            return IntegrationFlows.from(Amqp.inboundAdapter(rabbitConnectionFactory(), worksQueue()))
                    .log("Message", message -> message.getPayload())
                    .get();
        }
    }
}
