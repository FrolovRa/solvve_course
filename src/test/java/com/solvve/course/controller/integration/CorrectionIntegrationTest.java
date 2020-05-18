package com.solvve.course.controller.integration;

import com.solvve.course.BaseTest;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test", "integration-test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CorrectionIntegrationTest extends BaseTest {

    @Test
    public void testGetAllCorrections() {

    }
}
