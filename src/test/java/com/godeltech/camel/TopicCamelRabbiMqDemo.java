package com.godeltech.camel;

import com.godeltech.camel.rabbitmq.topics.TopicConsumer;
import com.godeltech.camel.rabbitmq.topics.TopicProducer;
import org.apache.camel.*;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import static com.godeltech.camel.rabbitmq.topics.TopicProducer.FIRE_BY_TOPIC_URL;

public class TopicCamelRabbiMqDemo extends CamelTestSupport {

    @Produce
    private ProducerTemplate producerTemplate;

    @Test
    public void demoFireBusinessEvent() throws Exception {
        publish("business.user.login", "The Business Event");
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RoutesBuilder[]{
                new TopicProducer(),
                new TopicConsumer("business.user.register", exchange -> System.out.println("Consuming: business.user.register")),
                new TopicConsumer("business.user.*", exchange -> System.out.println("Consuming: business.user.*")),
                new TopicConsumer("business.#", exchange -> System.out.println("Consuming: business.#")),
                new TopicConsumer("#", exchange -> System.out.println("Consuming: #"))
        };
    }

    public void publish(final String topic, final Object body) {
        producerTemplate.sendBodyAndHeader(FIRE_BY_TOPIC_URL, body, RabbitMQConstants.ROUTING_KEY, topic);
    }

}
