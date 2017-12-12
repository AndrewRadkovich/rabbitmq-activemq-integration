package com.godeltech.camel.activemq;

import com.godeltech.camel.activemq.async.AsyncConsumer;
import com.godeltech.camel.activemq.async.AsyncProducer;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class AsyncCunsumerActiveMqDemo extends CamelTestSupport {

    @Test
    public void demo() throws Exception {
        sendBody(AsyncProducer.ASYNC_PRODUCER_URI, "Test");
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RoutesBuilder[]{
                new AsyncProducer(),
                new AsyncConsumer()
        };
    }


    @Override
    protected CamelContext createCamelContext() throws Exception {
        final CamelContext context = super.createCamelContext();
        context.addComponent("activemq", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));
        return context;
    }

}
