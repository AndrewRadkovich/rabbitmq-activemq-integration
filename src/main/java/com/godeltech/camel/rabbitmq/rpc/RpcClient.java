package com.godeltech.camel.rabbitmq.rpc;

import org.apache.camel.builder.RouteBuilder;

public class RpcClient extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jetty:http://localhost:10223/topicservice?input={input}")
                .setBody().simple("${in.header.input}")
                .to("rabbitmq://localhost/rpc?queue=rpc_queue&username=user&password=bitnami");
    }

}
