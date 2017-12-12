package com.godeltech.camel.activemq.pubsub;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class TopicSubscriber extends RouteBuilder {

    private final String topic;
    private final Processor processor;

    public TopicSubscriber(String topic, Processor processor) {
        this.topic = topic;
        this.processor = processor;
    }

    @Override
    public void configure() throws Exception {
        fromF("activemq:topic:%s", topic).process(processor);
    }

}
