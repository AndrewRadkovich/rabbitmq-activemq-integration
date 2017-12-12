package com.godeltech.camel.activemq.async;

import org.apache.camel.builder.RouteBuilder;

public class AsyncConsumer extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("activemq:queue:start?asyncConsumer=true")
                .delay(2000)
                .log("************************")
                .log("* ${body} is processed *")
                .log("************************")
                .end();
    }

}
