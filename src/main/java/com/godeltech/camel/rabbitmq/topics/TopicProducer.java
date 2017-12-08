package com.godeltech.camel.rabbitmq.topics;

import org.apache.camel.builder.RouteBuilder;

public class TopicProducer extends RouteBuilder {

    public static final String FIRE_BY_TOPIC_URL = "direct:fireByTopic";

    @Override
    public void configure() throws Exception {
        from(FIRE_BY_TOPIC_URL)
                .to("rabbitmq://localhost:5672/directevents?" +
                        "username=user&" +
                        "password=bitnami&" +
                        "exchangeType=topic");
    }
}
