package com.godeltech.camel.rabbitmq.routing;

import org.apache.camel.builder.RouteBuilder;

public class DirectProducer extends RouteBuilder {

    public static final String ROUTED_EVENT_URL = "direct:fireEvent";

    @Override
    public void configure() throws Exception {
        from(ROUTED_EVENT_URL)
                .to("rabbitmq://localhost:5672/directevents?" +
                        "username=user&" +
                        "password=bitnami&" +
                        "exchangeType=direct");
    }
}
