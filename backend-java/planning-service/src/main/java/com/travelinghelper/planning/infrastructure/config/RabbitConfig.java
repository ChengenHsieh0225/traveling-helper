package com.travelinghelper.planning.infrastructure.config;

import com.travelinghelper.planning.domain.event.PlanDeletedEvent;
import com.travelinghelper.planning.domain.event.PlanHeaderUpdatedEvent;
import com.travelinghelper.planning.domain.event.PlanItineraryChangedEvent;
import com.travelinghelper.planning.domain.event.PlanPublishedEvent;
import com.travelinghelper.planning.infrastructure.message.MessagingConstants;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultJacksonJavaTypeMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange planningExchange() {
        return new TopicExchange(MessagingConstants.EXCHANGE);
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
