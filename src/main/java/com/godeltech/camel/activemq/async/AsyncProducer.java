package com.godeltech.camel.activemq.async;

import org.apache.camel.builder.RouteBuilder;

public class AsyncProducer extends RouteBuilder {

    public static final String ASYNC_PRODUCER_URI = "direct:asyncProducer";

    @Override
    public void configure() throws Exception {
        from(ASYNC_PRODUCER_URI)
                .to("activemq:queue:start")
                .log("****************")
                .log("* I'm finished *")
                .log("****************");
    }

}
