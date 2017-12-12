package com.godeltech.camel.activemq;

import com.godeltech.camel.activemq.pubsub.TopicSubscriber;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class TopicCamelActiveMqDemo extends CamelTestSupport {

    @Test
    public void demoToAllConsumers() throws Exception {
        sendBody("activemq:topic:business.user.register", "Test");
    }

    @Test
    public void demoToOneConsumer() throws Exception {
        sendBody("activemq:topic:business.task", "Test");
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RoutesBuilder[]{
                new TopicSubscriber("business.user.register", log("Consuming #1")),
                new TopicSubscriber("business.user.*", log("Consuming #2")),
                new TopicSubscriber("business.>", log("Consuming #3")),
                new TopicSubscriber(">", log("Consuming #4"))
        };
    }

    private Processor log(String message) {
        return exchange -> {
            System.out.println("*****************************");
            System.out.println("* " + message + " : " + exchange.getIn().getBody(String.class) + " *");
            System.out.println("*****************************");
        };
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        final CamelContext context = super.createCamelContext();
        context.addComponent("activemq", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));
        return context;
    }

}
