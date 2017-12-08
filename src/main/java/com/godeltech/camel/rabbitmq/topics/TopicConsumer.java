package com.godeltech.camel.rabbitmq.topics;

import com.godeltech.camel.ConsumerBean;
import org.apache.camel.builder.RouteBuilder;

public class TopicConsumer extends RouteBuilder {
    private final ConsumerBean consumer;
    private final String routingKey;

    public TopicConsumer(String routingKey, ConsumerBean consumer) {
        this.consumer = consumer;
        this.routingKey = routingKey;
    }

    @Override
    public void configure() throws Exception {
        fromF("rabbitmq://localhost:5672/directevents?username=user&password=bitnami&exchangeType=topic&" +
                "routingKey=%s", routingKey)
                .bean(consumer, "process");
    }
}
