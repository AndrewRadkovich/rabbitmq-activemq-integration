package com.godeltech.camel;

import com.godeltech.camel.rabbitmq.topics.TopicConsumer;
import com.godeltech.camel.rabbitmq.topics.TopicProducer;
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
    public void demoFireSystemEvent() throws Exception {
        publish("system", "The System Event");
    }

    @Test
    public void demoFireBusinessEvent() throws Exception {
        publish("businessevent", "The Business Event");
    }

    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RoutesBuilder[]{
                new TopicProducer(),
                new TopicConsumer("system.app1.exception", new ConsumerBean()),
                new TopicConsumer("system.app1.executor1.started", new ConsumerBean()),
                new TopicConsumer("system.*.*.*", new ConsumerBean()),
                new TopicConsumer("business.user.register", new ConsumerBean()),
                new TopicConsumer("business.user.*", new ConsumerBean()),
                new TopicConsumer("business.#", new ConsumerBean()),
                new TopicConsumer("#", new ConsumerBean())
        };
    }

    public void publish(final String topic, final Object body) {
        producerTemplate.sendBodyAndHeader(FIRE_BY_TOPIC_URL, body, RabbitMQConstants.ROUTING_KEY, topic);
    }

}
