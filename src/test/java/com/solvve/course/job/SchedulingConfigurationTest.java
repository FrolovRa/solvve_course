package com.solvve.course.job;

import com.solvve.course.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@Import(SchedulingConfigurationTest.ScheduledTestConfig.class)
public class SchedulingConfigurationTest extends BaseTest {

    @Test
    public void testSpringContextUpAndRunning() {
        log.info("@Scheduling configuration are OK");
    }

    @EnableScheduling
    static class ScheduledTestConfig {
    }
}