package com.solvve.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvve.course.util.TestUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected TestUtils utils = new TestUtils();

    @Value("${spring.data.web.pageable.default-page-size}")
    protected int defaultPageSize;

    @Value("${spring.data.web.pageable.max-page-size}")
    protected int maxPageSize;
}
