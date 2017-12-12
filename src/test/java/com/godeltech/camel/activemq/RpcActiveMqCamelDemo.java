package com.godeltech.camel.activemq;

import com.godeltech.camel.activemq.rpc.RpcServer;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class RpcActiveMqCamelDemo extends CamelTestSupport {

    @Test
    public void demo() throws Exception {
        final Integer reply = template.requestBody("activemq:queue:pow2", 3, Integer.class);
        System.out.println("reply = " + reply);
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RoutesBuilder[]{
                new RpcServer("pow2", exchange -> {
                    final Integer in = exchange.getIn().getBody(Integer.class);
                    exchange.getIn().setBody(in * in);
                })
        };
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        final CamelContext context = super.createCamelContext();
        context.addComponent("activemq", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));
        return context;
    }
}
