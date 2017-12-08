package com.godeltech.camel.rabbitmq.pubsub;

import org.apache.camel.builder.RouteBuilder;

public class FanoutProducer extends RouteBuilder {

    public static final String FIRE_EVENT_TO_ALL_URL = "direct:fireEventToAll";

    @Override
    public void configure() throws Exception {
        from(FIRE_EVENT_TO_ALL_URL)
                .to("rabbitmq://localhost:5672/fanoutevents?username=user&password=bitnami&exchangeType=fanout");
    }

}
