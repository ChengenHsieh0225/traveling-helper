package com.travelinghelper.social.infrastructure.message;

import com.travelinghelper.social.application.service.SyncService;
import com.travelinghelper.social.domain.event.PlanDeletedEvent;
import com.travelinghelper.social.domain.event.PlanHeaderUpdatedEvent;
import com.travelinghelper.social.domain.event.PlanItineraryChangedEvent;
import com.travelinghelper.social.domain.event.PlanPublishedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "social.sync-plan.queue")
@RequiredArgsConstructor
public class PlanSyncListener {

    private final SyncService syncService;

    @RabbitHandler
    public void handlePublished(PlanPublishedEvent event) {
        syncService.syncFromPlanPublishedEvent(event);
    }

    @RabbitHandler
    public void handleDeleted(PlanDeletedEvent event) {
        syncService.syncFromDeletedEvent(event);
    }

    @RabbitHandler
    public void handleHeaderUpdated(PlanHeaderUpdatedEvent event) {
        syncService.syncFromPlanHeaderUpdatedEvent(event);
    }

    @RabbitHandler
    public void handleItineraryChanged(PlanItineraryChangedEvent event) {
        syncService.syncFromItineraryChangedEvent(event);
    }
}