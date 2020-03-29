package com.solvve.course.repository;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.Principal;
import com.solvve.course.exception.EntityNotFoundException;
import org.junit.Test;

import java.util.UUID;

public class RepositoryHelperTest extends BaseTest {

    @Test(expected = EntityNotFoundException.class)
    public void testGetReferenceNotExist() {
        repositoryHelper.getReferenceIfExist(Principal.class, UUID.randomUUID());
    }
}
