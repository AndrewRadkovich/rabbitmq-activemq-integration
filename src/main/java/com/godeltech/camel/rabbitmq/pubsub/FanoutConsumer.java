package com.godeltech.camel.rabbitmq.pubsub;

import com.godeltech.camel.ConsumerBean;
import org.apache.camel.builder.RouteBuilder;

public class FanoutConsumer extends RouteBuilder {

    private final ConsumerBean consumer;
    private final String eventName;

    public FanoutConsumer(String eventName, ConsumerBean consumer) {
        this.consumer = consumer;
        this.eventName = eventName;
    }

    @Override
    public void configure() throws Exception {
        fromF("rabbitmq://localhost:5672/fanoutevents?username=user&password=bitnami&exchangeType=fanout&routingKey=%s", eventName)
                .bean(consumer, "process");
    }
}