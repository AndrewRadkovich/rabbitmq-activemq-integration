package com.godeltech.camel.rabbitmq.topics;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class TopicConsumer extends RouteBuilder {
    private final String routingKey;
    private final Processor processor;

    public TopicConsumer(String routingKey, Processor processor) {
        this.routingKey = routingKey;
        this.processor = processor;
    }

    @Override
    public void configure() throws Exception {
        fromF("rabbitmq://localhost:5672/directevents?username=user&password=bitnami&exchangeType=topic&" +
                "routingKey=%s", routingKey)
                .process(processor);
    }
}
