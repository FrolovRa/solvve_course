package com.solvve.course.event.listener;

import com.solvve.course.event.AcceptCorrectionEvent;
import com.solvve.course.service.PublicationCorrectionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ChangeSimilarCorrectionsListenerTest {

    @MockBean
    private PublicationCorrectionService publicationCorrectionService;

    @SpyBean
    private ChangeSimilarCorrectionsListener changeSimilarCorrectionsListener;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    public void testOnEvent() {
        final UUID correctionId = UUID.randomUUID();
        AcceptCorrectionEvent event = new AcceptCorrectionEvent(correctionId);

        applicationEventPublisher.publishEvent(event);

        verify(changeSimilarCorrectionsListener, timeout(500)).onEvent(event);
        verify(publicationCorrectionService, timeout(500)).changeSimilarCorrections(correctionId);
    }

    @Test
    public void testOnEventAsync() throws InterruptedException {
        final UUID correctionId = UUID.randomUUID();
        AcceptCorrectionEvent event = new AcceptCorrectionEvent(correctionId);
        List<Integer> checkList = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            Thread.sleep(500);
            checkList.add(2);
            latch.countDown();
            return null;
        }).when(publicationCorrectionService).changeSimilarCorrections(correctionId);

        applicationEventPublisher.publishEvent(event);
        checkList.add(1);
        latch.await();

        verify(changeSimilarCorrectionsListener).onEvent(event);
        verify(publicationCorrectionService).changeSimilarCorrections(correctionId);
        assertEquals(Arrays.asList(1, 2), checkList);
    }
}
