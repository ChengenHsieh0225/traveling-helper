package com.travelinghelper.planning.application.event;

import com.travelinghelper.planning.application.event.ApplicationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InternalApplicationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(ApplicationEvent event) {
        this.eventPublisher.publishEvent(event);
    }
}
