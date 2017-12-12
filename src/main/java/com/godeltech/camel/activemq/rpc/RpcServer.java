package com.godeltech.camel.activemq.rpc;

import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class RpcServer extends RouteBuilder {

    private final Processor processor;
    private final String rpcName;

    public RpcServer(String rpcName, Processor processor) {
        this.processor = processor;
        this.rpcName = rpcName;
    }

    @Override
    public void configure() throws Exception {
        fromF("activemq:queue:%s", rpcName).process(processor).log("${body}").end();
    }
}
