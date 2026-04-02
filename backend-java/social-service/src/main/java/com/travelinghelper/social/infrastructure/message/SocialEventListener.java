package com.travelinghelper.social.infrastructure.message;

import com.travelinghelper.social.application.service.SyncService;
import com.travelinghelper.social.domain.event.PlanPublishedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "social.sync-plan.queue")
@RequiredArgsConstructor
public class SocialEventListener {

    private final SyncService syncService;

    @RabbitHandler
    public void handlePublished(PlanPublishedEvent event) {
        syncService.syncFromEvent(event);
    }
}
