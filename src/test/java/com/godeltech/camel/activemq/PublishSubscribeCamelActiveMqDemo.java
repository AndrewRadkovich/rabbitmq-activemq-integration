package com.godeltech.camel.activemq;

import com.godeltech.camel.activemq.pubsub.TopicSubscriber;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class PublishSubscribeCamelActiveMqDemo extends CamelTestSupport {

    @Test
    public void demo() throws Exception {
        sendBody("activemq:topic:topicName", "Test");
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RoutesBuilder[]{
                new TopicSubscriber("topicName", log("Consuming #1")),
                new TopicSubscriber("topicName", log("Consuming #2")),
                new TopicSubscriber("topicName", log("Consuming #3"))
        };
    }

    private Processor log(String message) {
        return exchange -> System.out.println(message + " : " + exchange.getIn().getBody(String.class));
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        final CamelContext context = super.createCamelContext();
        context.addComponent("activemq", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));
        return context;
    }

}
