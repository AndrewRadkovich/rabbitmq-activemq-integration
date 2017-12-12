package com.godeltech.camel.rabbitmq;

import com.godeltech.camel.ConsumerBean;
import com.godeltech.camel.rabbitmq.pubsub.FanoutProducer;
import com.godeltech.camel.rabbitmq.pubsub.FanoutConsumer;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import static com.godeltech.camel.rabbitmq.pubsub.FanoutProducer.FIRE_EVENT_TO_ALL_URL;
import static com.godeltech.camel.rabbitmq.routing.DirectProducer.ROUTED_EVENT_URL;

public class PublishSubscribeCamelRabbiMqDemo extends CamelTestSupport {

    @Produce
    private ProducerTemplate producerTemplate;

    @Test
    public void demo() throws Exception {
        publishToAll("body");
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RoutesBuilder[]{
                new FanoutProducer(),
                new FanoutConsumer("SYSTEMEVENTS", new ConsumerBean()),
                new FanoutConsumer("BUSINESSEVENTS", new ConsumerBean())
        };
    }

    public void publishToAll(final Object body) {
        producerTemplate.sendBody(FIRE_EVENT_TO_ALL_URL, body);
    }

    public void publichTo(final String routingKey, final Object body) {
        producerTemplate.sendBodyAndHeader(ROUTED_EVENT_URL, body, RabbitMQConstants.ROUTING_KEY, routingKey);
    }

    public void publish(final String topic, final Object body) {
        producerTemplate.sendBodyAndHeader(ROUTED_EVENT_URL, body, RabbitMQConstants.ROUTING_KEY, topic);
    }

}
