package com.godeltech.springintegration.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.jms.ConnectionFactory;
import java.util.Arrays;
import java.util.List;

@Configuration
@ImportAutoConfiguration({ActiveMQAutoConfiguration.class, JmxAutoConfiguration.class, IntegrationAutoConfiguration.class})
@IntegrationComponentScan
public class SpringIntegrationActiveMqDemo {

    public static void main(String[] args) throws InterruptedException {
        final ConfigurableApplicationContext ctx = SpringApplication.run(SpringIntegrationActiveMqDemo.class, args);
        final List<String> body = Arrays.asList("foo", "bar");

        final MessageChannel requestChannel = ctx.getBean("requestChannel", MessageChannel.class);
        final QueueChannel replyChannel = ctx.getBean("replyChannel", QueueChannel.class);

        requestChannel.send(MessageBuilder.withPayload(body).build());

        final Message<List<String>> reply = (Message<List<String>>) replyChannel.receive(600000);

        System.out.println("reply = " + reply.getPayload());

        ctx.close();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory();
    }

    @Bean
    public IntegrationFlow jmsOutboundFlow() {
        return f -> f
                .channel(requestChannel())
                .handleWithAdapter(h -> h.jms(connectionFactory()).destination(new ActiveMQQueue("queueName")));
    }

    @Bean
    public QueueChannel replyChannel() {
        return new QueueChannel(100);
    }

    @Bean
    public MessageChannel requestChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow jmsInboundFlow() {
        return IntegrationFlows
                .from(Jms.inboundGateway(connectionFactory()).destination(new ActiveMQQueue("queueName")))
                .log("payload")
                .split()
                .<String, String>transform(String::toUpperCase)
                .aggregate()
                .log("payload")
                .channel(replyChannel()).get();
    }
}