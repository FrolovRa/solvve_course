package com.solvve.course.controller;

import com.solvve.course.BaseControllerTest;
import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.service.PrincipalService;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PrincipalController.class)
public class NoSessionTest extends BaseControllerTest {

    @MockBean
    private PrincipalService principalService;

    @Test
    public void testNoSession() throws Exception {
        final UUID wrongId = UUID.randomUUID();
        when(principalService.getPrincipal(wrongId)).thenReturn(new PrincipalReadDto());

        MvcResult result = mvc.perform(get("/api/v1/principals/{id}", wrongId))
            .andExpect(status().isOk())
            .andReturn();

        assertNull(result.getRequest().getSession(false));
    }
}
