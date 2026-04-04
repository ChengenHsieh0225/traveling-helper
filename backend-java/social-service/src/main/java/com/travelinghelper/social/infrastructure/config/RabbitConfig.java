package com.travelinghelper.social.infrastructure.config;

import com.travelinghelper.social.domain.event.PlanPublishedEvent;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String SYNC_QUEUE = "social.sync-plan.queue";
    public static final String PLANNING_EXCHANGE = "planning.exchange";
    public static final String ROUTING_KEY = "plan.published";

    @Bean
    public Queue syncQueue() {
        return QueueBuilder.durable(SYNC_QUEUE)
            .withArgument("x-dead-letter-exchange", "social.dead.letter.exchange") // 設定死信佇列
            .build();
    }

    @Bean
    public TopicExchange planningExchange() {
        return new TopicExchange(PLANNING_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue syncQueue, TopicExchange planningExchange) {
        return BindingBuilder.bind(syncQueue).to(planningExchange).with(ROUTING_KEY);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();

        // Create a class mapper from event classes defined in other microservices
        DefaultClassMapper classMapper = new DefaultClassMapper();

        // Map the label to local event classes
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("planning.plan.published", PlanPublishedEvent.class);

        classMapper.setIdClassMapping(idClassMapping);
        converter.setClassMapper(classMapper);

        return converter;
    }
}
