package com.solvve.course.event.listener;

import com.solvve.course.event.CorrectionStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotifyUserOnStatusAcceptedListener {

    @Async
    @EventListener(condition = "#event.newStatus == T(com.solvve.course.domain.constant.CorrectionStatus).ACCEPTED ||"
            + "#event.newStatus == T(com.solvve.course.domain.constant.CorrectionStatus).ACCEPTED_AFTER_FIX")
    public void onEvent(CorrectionStatusChangedEvent event) {
        log.info("handling {}", event);
    }
}
