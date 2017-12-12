package com.godeltech.camel.rabbitmq.rpc;

import org.apache.camel.builder.RouteBuilder;

public class RpcServer extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("rabbitmq://localhost/rpc?queue=rpc_queue&username=user&password=bitnami")
                .setBody().simple("${in.body} is processed")
                .to("stream:out");
    }

}
