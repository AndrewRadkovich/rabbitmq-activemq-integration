package com.godeltech.camel.rabbitmq;

import com.godeltech.camel.ConsumerBean;
import com.godeltech.camel.rabbitmq.routing.DirectConsumer;
import com.godeltech.camel.rabbitmq.routing.DirectProducer;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import static com.godeltech.camel.rabbitmq.routing.DirectProducer.ROUTED_EVENT_URL;

public class RoutingCamelRabbiMqDemo extends CamelTestSupport {

    @Produce
    private ProducerTemplate producerTemplate;

    @Test
    public void demoFireSystemEvent() throws Exception {
        publishTo("systemevent", "The System Event");
    }

    @Test
    public void demoFireBusinessEvent() throws Exception {
        publishTo("businessevent", "The Business Event");
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RoutesBuilder[]{
                new DirectProducer(),
                new DirectConsumer("systemevent", new ConsumerBean()),
                new DirectConsumer("businessevent", new ConsumerBean())
        };
    }

    private void publishTo(final String routingKey, final Object body) {
        producerTemplate.sendBodyAndHeader(ROUTED_EVENT_URL, body, RabbitMQConstants.ROUTING_KEY, routingKey);
    }
}
