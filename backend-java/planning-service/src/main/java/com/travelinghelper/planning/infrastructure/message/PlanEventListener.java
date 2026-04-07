package com.travelinghelper.planning.infrastructure.message;

import com.travelinghelper.planning.domain.event.PlanPublishedEvent;
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
    public void handlerPlanPublished(PlanPublishedEvent event) {
        this.rabbitTemplate.convertAndSend(
            MessagingConstants.EXCHANGE,
            MessagingConstants.RoutingKeys.PLAN_PUBLISHED,
            event
        );
    }
}
