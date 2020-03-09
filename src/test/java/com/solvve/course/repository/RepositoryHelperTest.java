package com.solvve.course.repository;

import com.solvve.course.domain.Principal;
import com.solvve.course.exception.EntityNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest
@RunWith(SpringRunner.class)
public class RepositoryHelperTest {

    @Autowired
    private RepositoryHelper repositoryHelper;

    @Test(expected = EntityNotFoundException.class)
    public void testGetReferenceNotExist() {
        repositoryHelper.getReferenceIfExist(Principal.class, UUID.randomUUID());
    }
}
