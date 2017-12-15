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
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.support.PeriodicTrigger;

import javax.jms.ConnectionFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Configuration
@ImportAutoConfiguration({ActiveMQAutoConfiguration.class, JmxAutoConfiguration.class, IntegrationAutoConfiguration.class})
@IntegrationComponentScan
public class SpringIntegrationActiveMqDemo {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext ctx = SpringApplication.run(SpringIntegrationActiveMqDemo.class, args);
        List<String> strings = Arrays.asList("foo", "bar");
        final ActiveMqGateway activeMqGateway = ctx.getBean(ActiveMqGateway.class);
        activeMqGateway.send(strings);
        ctx.close();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory();
    }

    @MessagingGateway
    public interface ActiveMqGateway {
        @Gateway(requestChannel = "jmsOutboundFlow.input")
        Collection<String> send(Collection<String> strings);
    }

    @Bean
    public IntegrationFlow jmsOutboundFlow() {
        return f -> f.handleWithAdapter(h -> h
                .jms(connectionFactory())
                .destination(new ActiveMQQueue("queueName"))
        );
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {

        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(10));
        return pollerMetadata;
    }

    @Bean
    public IntegrationFlow jmsInboundFlow() {
        return IntegrationFlows
                .from(Jms.pollableChannel(connectionFactory()).destination(new ActiveMQQueue("queueName")))
                .log("payload")
                .split()
                .<String, String>transform(String::toUpperCase)
                .aggregate()
                .log("payload")
                .get();
    }
}