package com.godeltech.camel.rabbitmq;

import com.godeltech.camel.rabbitmq.topics.TopicConsumer;
import com.godeltech.camel.rabbitmq.topics.TopicProducer;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
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
                new TopicConsumer("business.user.register", log("Consuming: business.user.register")),
                new TopicConsumer("business.user.*", log("Consuming: business.user.*")),
                new TopicConsumer("business.#", log("Consuming: business.#")),
                new TopicConsumer("#", log("Consuming: #"))
        };
    }

    private Processor log(String message) {
        return exchange -> System.out.println(message);
    }

    private void publish(final String topic, final Object body) {
        producerTemplate.sendBodyAndHeader(FIRE_BY_TOPIC_URL, body, RabbitMQConstants.ROUTING_KEY, topic);
    }

}
