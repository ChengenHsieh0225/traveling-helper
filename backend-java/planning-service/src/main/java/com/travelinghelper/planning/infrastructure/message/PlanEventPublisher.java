package com.travelinghelper.planning.infrastructure.message;

import com.travelinghelper.planning.domain.event.PlanPublishedEvent;
import com.travelinghelper.planning.infrastructure.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishPlanPublished(PlanPublishedEvent event) {
        this.rabbitTemplate.convertAndSend(
            MessagingConstants.EXCHANGE,
            MessagingConstants.RoutingKeys.PLAN_PUBLISHED,
            event
        );
    }
}
