package com.solvve.course.controller;

import com.solvve.course.BaseControllerTest;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithAnonymousUser
@WebMvcTest(controllers = HealthController.class)
public class HealthControllerTest extends BaseControllerTest {

    @Test
    public void testHealth() throws Exception {
        mvc.perform(get("/health"))
            .andExpect(status().isOk());
    }
}
