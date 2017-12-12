package com.godeltech.camel.activemq.topic;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class TopicConsumer extends RouteBuilder {

    private final String topic;
    private final Processor processor;

    public TopicConsumer(String topic, Processor processor) {
        this.topic = topic;
        this.processor = processor;
    }

    @Override
    public void configure() throws Exception {
        fromF("activemq:topic:%s", topic).process(processor);
    }

}
