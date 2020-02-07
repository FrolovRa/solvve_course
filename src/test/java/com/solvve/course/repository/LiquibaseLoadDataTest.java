package com.solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml")
public class LiquibaseLoadDataTest {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private PrincipalRepository principalRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testDataLoaded() {
        assertTrue(movieRepository.count() > 0);
        assertTrue(actorRepository.count() > 0);
        assertTrue(personRepository.count() > 0);
        assertTrue(characterRepository.count() > 0);
        assertTrue(principalRepository.count() > 0);
        assertTrue(userRepository.count() > 0);
    }
}
