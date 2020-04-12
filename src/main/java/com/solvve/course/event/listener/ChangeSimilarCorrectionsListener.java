package com.solvve.course.event.listener;

import com.solvve.course.event.AcceptCorrectionEvent;
import com.solvve.course.service.PublicationCorrectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChangeSimilarCorrectionsListener {

    @Autowired
    private PublicationCorrectionService publicationCorrectionService;

    @Async
    @EventListener
    public void onEvent(AcceptCorrectionEvent event) {
        log.info("handling {}", event);

        publicationCorrectionService.changeSimilarCorrections(event.getCorrectionId());
    }
}