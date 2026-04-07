package com.travelinghelper.social.infrastructure.config;

import com.travelinghelper.social.domain.event.PlanDeletedEvent;
import com.travelinghelper.social.domain.event.PlanHeaderUpdatedEvent;
import com.travelinghelper.social.domain.event.PlanItineraryChangedEvent;
import com.travelinghelper.social.domain.event.PlanPublishedEvent;
import com.travelinghelper.social.infrastructure.message.MessagingConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.DefaultJacksonJavaTypeMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {
    @Bean
    public Queue syncQueue() {
        return QueueBuilder.durable(MessagingConstants.SYNC_QUEUE)
            .withArgument("x-dead-letter-exchange", "social.dead.letter.exchange") // Set the dead-letter queue (DLQ)
            .build();
    }

    @Bean
    public TopicExchange planningExchange() {
        return new TopicExchange(MessagingConstants.EXCHANGE);
    }

    @Bean
    public Binding binding(Queue syncQueue, TopicExchange planningExchange) {
        return BindingBuilder.bind(syncQueue).to(planningExchange).with(MessagingConstants.RoutingKeys.PLAN_ALL_PATTERN);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();

        // Create a type mapper from event classes defined in other microservices
        DefaultJacksonJavaTypeMapper typeMapper = new DefaultJacksonJavaTypeMapper();

        // Map the label to local event classes
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put(MessagingConstants.TypeIds.PLAN_PUBLISHED, PlanPublishedEvent.class);
        idClassMapping.put(MessagingConstants.TypeIds.PLAN_HEADER_UPDATED, PlanHeaderUpdatedEvent.class);
        idClassMapping.put(MessagingConstants.TypeIds.PLAN_ITINERARY_CHANGED, PlanItineraryChangedEvent.class);
        idClassMapping.put(MessagingConstants.TypeIds.PLAN_DELETED, PlanDeletedEvent.class);

        typeMapper.setIdClassMapping(idClassMapping);
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }
}
