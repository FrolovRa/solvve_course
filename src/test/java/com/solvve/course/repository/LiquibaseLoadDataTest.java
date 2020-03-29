package com.solvve.course.repository;

import com.solvve.course.BaseTest;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import static org.junit.Assert.assertTrue;

@TestPropertySource(properties = "spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml")
public class LiquibaseLoadDataTest extends BaseTest {

    @Test
    public void testDataLoaded() {
        assertTrue(movieRepository.count() > 0);
        assertTrue(actorRepository.count() > 0);
        assertTrue(personRepository.count() > 0);
        assertTrue(characterRepository.count() > 0);
        assertTrue(principalRepository.count() > 0);
        assertTrue(userRepository.count() > 0);
        assertTrue(publicationRepository.count() > 0);
        assertTrue(correctionRepository.count() > 0);
        assertTrue(ratingRepository.count() > 0);
    }
}
