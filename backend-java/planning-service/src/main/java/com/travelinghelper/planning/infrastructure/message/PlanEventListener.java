package com.travelinghelper.planning.infrastructure.message;

import com.travelinghelper.planning.domain.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PlanEventListener {

    private final RabbitTemplate rabbitTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePlanPublished(PlanPublishedEvent event) {
        this.rabbitTemplate.convertAndSend(
            MessagingConstants.EXCHANGE,
            MessagingConstants.RoutingKeys.PLAN_PUBLISHED,
            event
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePlanHeaderUpdated(PlanHeaderUpdatedEvent event) {
        this.rabbitTemplate.convertAndSend(
            MessagingConstants.EXCHANGE,
            MessagingConstants.RoutingKeys.PLAN_HEADER_UPDATED,
            event
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePlanItineraryChanged(PlanItineraryChangedEvent event) {
        this.rabbitTemplate.convertAndSend(
            MessagingConstants.EXCHANGE,
            MessagingConstants.RoutingKeys.PLAN_ITINERARY_CHANGED,
            event
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePlanDeleted(PlanDeletedEvent event) {
        this.rabbitTemplate.convertAndSend(
            MessagingConstants.EXCHANGE,
            MessagingConstants.RoutingKeys.PLAN_DELETED,
            event
        );
    }
}
