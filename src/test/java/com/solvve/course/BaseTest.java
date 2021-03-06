package com.solvve.course;

import com.solvve.course.repository.*;
import com.solvve.course.security.CurrentPrincipalValidator;
import com.solvve.course.security.UserDetailServiceImpl;
import com.solvve.course.service.*;
import com.solvve.course.service.importer.ExternalSystemImportService;
import com.solvve.course.service.importer.MovieImporterService;
import com.solvve.course.util.TestUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {
    "delete from movie_actor",
    "delete from movie_star_actor",
    "delete from movie_genres",
    "delete from correction",
    "delete from rating",
    "delete from user",
    "delete from publication",
    "delete from principal",
    "delete from character",
    "delete from actor",
    "delete from person",
    "delete from movie",
    "delete from external_system_import"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class BaseTest {

    @Autowired
    protected MovieRepository movieRepository;

    @Autowired
    protected ActorRepository actorRepository;

    @Autowired
    protected PersonRepository personRepository;

    @Autowired
    protected CharacterRepository characterRepository;

    @Autowired
    protected PrincipalRepository principalRepository;

    @Autowired
    protected PrincipalRoleRepository principalRoleRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CorrectionRepository correctionRepository;

    @Autowired
    protected PublicationRepository publicationRepository;

    @Autowired
    protected RatingRepository ratingRepository;

    @Autowired
    protected MovieService movieService;

    @Autowired
    protected ActorService actorService;

    @Autowired
    protected PersonService personService;

    @Autowired
    protected CharacterService characterService;

    @Autowired
    protected PrincipalService principalService;

    @Autowired
    protected PrincipalRoleService principalRoleService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected CorrectionService correctionService;

    @Autowired
    protected PublicationService publicationService;

    @Autowired
    protected PublicationCorrectionService publicationCorrectionService;

    @Autowired
    protected MovieImporterService movieImporterService;

    @Autowired
    protected ExternalSystemImportRepository externalSystemImportRepository;

    @Autowired
    protected ExternalSystemImportService externalSystemImportService;

    @Autowired
    protected UserDetailServiceImpl userDetailService;

    @Autowired
    protected RepositoryHelper repositoryHelper;

    @Autowired
    protected TranslationService translationService;

    @Autowired
    protected CurrentPrincipalValidator currentPrincipalValidator;

    @Autowired
    protected TestUtils utils;
}
