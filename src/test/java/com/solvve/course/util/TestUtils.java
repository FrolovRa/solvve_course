package com.solvve.course.util;

import com.solvve.course.domain.Character;
import com.solvve.course.domain.*;
import com.solvve.course.domain.constant.CorrectionStatus;
import com.solvve.course.dto.actor.*;
import com.solvve.course.dto.character.CharacterCreateDto;
import com.solvve.course.dto.character.CharacterPatchDto;
import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.dto.correction.CorrectionCreateDto;
import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.person.PersonCreateDto;
import com.solvve.course.dto.person.PersonPatchDto;
import com.solvve.course.dto.person.PersonReadDto;
import com.solvve.course.dto.principal.PrincipalCreateDto;
import com.solvve.course.dto.principal.PrincipalPatchDto;
import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.dto.publication.PublicationCreateDto;
import com.solvve.course.dto.publication.PublicationPatchDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.dto.user.UserPatchDto;
import com.solvve.course.dto.user.UserReadDto;
import com.solvve.course.repository.*;
import org.bitbucket.brunneng.br.Configuration;
import org.bitbucket.brunneng.br.RandomObjectGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

@Service
public class TestUtils {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private CorrectionRepository correctionRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private RandomObjectGenerator generator;

    {
        Configuration c = new Configuration();
        c.setFlatMode(true);
        generator = new RandomObjectGenerator(c);
    }

    private <T extends AbstractEntity> T generateFlatEntityWithoutId(Class<T> targetClass) {
        T testObject = generator.generateRandomObject(targetClass);
        testObject.setId(null);

        return testObject;
    }

    public Actor getActorFromDb() {
        Actor actor = new Actor();
        actor.setPerson(this.getPersonFromDb());

        return actorRepository.save(actor);
    }

    public Movie getMovieFromDb() {
        Movie movie = generateFlatEntityWithoutId(Movie.class);
        movie.setRating(null);

        return movieRepository.save(movie);
    }

    public Person getPersonFromDb() {
        Person person = generateFlatEntityWithoutId(Person.class);

        return personRepository.save(person);
    }

    public Character getCharacterFromDb() {
        Character character = generateFlatEntityWithoutId(Character.class);
        character.setActor(this.getActorFromDb());
        character.setMovie(this.getMovieFromDb());

        return characterRepository.save(character);
    }

    public User getUserFromDb() {
        User user = generateFlatEntityWithoutId(User.class);
        user.setPrincipal(this.getPrincipalFromDb());

        return userRepository.save(user);
    }

    public Principal getPrincipalFromDb() {
        Principal principal = generateFlatEntityWithoutId(Principal.class);

        return principalRepository.save(principal);
    }

    public Publication getPublicationFromDb() {
        Publication publication = generateFlatEntityWithoutId(Publication.class);
        publication.setManager(this.getPrincipalFromDb());
        publication.setContent("text content");

        return publicationRepository.save(publication);
    }

    public Correction getCorrectionFromDb() {
        Correction correction = new Correction();
        correction.setUser(this.getUserFromDb());
        correction.setStatus(CorrectionStatus.NEW);
        correction.setPublication(this.getPublicationFromDb());
        correction.setStartIndex(0);
        correction.setSelectedText("text");
        correction.setProposedText("fixed text");

        return correctionRepository.save(correction);
    }

    public PersonCreateDto createPersonCreateDto() {
        return generator.generateRandomObject(PersonCreateDto.class);
    }

    public CharacterCreateDto createCharacterCreateDto() {
        CharacterCreateDto dto = generator.generateRandomObject(CharacterCreateDto.class);
        dto.setActorId(this.getActorFromDb().getId());
        dto.setMovieId(this.getMovieFromDb().getId());

        return dto;
    }

    public CharacterPatchDto createCharacterPatchDto() {
        return generator.generateRandomObject(CharacterPatchDto.class);
    }

    public CharacterReadDto createCharacterReadDto() {
        CharacterReadDto characterReadDto = generator.generateRandomObject(CharacterReadDto.class);
        characterReadDto.setActor(createActorReadDto());

        return characterReadDto;
    }

    public ActorReadDto createActorReadDto() {
        return generator.generateRandomObject(ActorReadDto.class);
    }

    public MovieCreateDto createMovieCreateDto() {
        return generator.generateRandomObject(MovieCreateDto.class);
    }

    public UserCreateDto createUserCreateDto() {
        UserCreateDto dto = generator.generateRandomObject(UserCreateDto.class);
        dto.setPrincipalId(this.getPrincipalFromDb().getId());

        return dto;
    }

    public PrincipalCreateDto createPrincipalCreateDto() {
        return generator.generateRandomObject(PrincipalCreateDto.class);
    }

    public UserPatchDto createUserPatchDto() {
        return generator.generateRandomObject(UserPatchDto.class);
    }

    public UserReadDto createUserReadDto() {
        UserReadDto userReadDto = generator.generateRandomObject(UserReadDto.class);
        userReadDto.setPrincipal(createPrincipalReadDto());

        return userReadDto;
    }

    public PrincipalReadDto createPrincipalReadDto() {
        return generator.generateRandomObject(PrincipalReadDto.class);
    }

    public PrincipalPatchDto createPrincipalPatchDto() {
        return generator.generateRandomObject(PrincipalPatchDto.class);
    }

    public MovieReadDto createMovieReadDto() {
        return generator.generateRandomObject(MovieReadDto.class);
    }

    public PersonReadDto createPersonReadDto() {
        return generator.generateRandomObject(PersonReadDto.class);
    }

    public PersonPatchDto createPersonPatchDto() {
        PersonPatchDto personPatchDto = new PersonPatchDto();
        personPatchDto.setName("new Name");

        return personPatchDto;
    }

    public ActorExtendedReadDto createActorExtendedReadDto() {
        ActorExtendedReadDto actorExtendedReadDto = new ActorExtendedReadDto();
        actorExtendedReadDto.setId(UUID.randomUUID());
        actorExtendedReadDto.setPerson(this.createPersonReadDto());

        return actorExtendedReadDto;
    }

    public ActorPatchDto createActorPatchDto() {
        return generator.generateRandomObject(ActorPatchDto.class);
    }

    public ActorPutDto createActorPutDto() {
        return generator.generateRandomObject(ActorPutDto.class);
    }

    public ActorCreateDto createActorCreateDto() {
        return generator.generateRandomObject(ActorCreateDto.class);
    }

    public PublicationCreateDto createPublicationCreateDto() {
        PublicationCreateDto publicationCreateDto = generator.generateRandomObject(PublicationCreateDto.class);
        publicationCreateDto.setManagerId(getPrincipalFromDb().getId());

        return publicationCreateDto;
    }

    public CorrectionCreateDto createCorrectionCreateDto() {
        CorrectionCreateDto dto = generator.generateRandomObject(CorrectionCreateDto.class);
        dto.setUserId(this.createUserReadDto().getId());
        dto.setPublicationId(this.createPublicationReadDto().getId());

        return dto;
    }

    public PublicationReadDto createPublicationReadDto() {
        PublicationReadDto dto = generator.generateRandomObject(PublicationReadDto.class);
        dto.setManager(this.createPrincipalReadDto());

        return dto;
    }

    public PublicationPatchDto createPublicationPatchDto() {
        return generator.generateRandomObject(PublicationPatchDto.class);
    }

    public CorrectionReadDto createCorrectionReadDto() {
        CorrectionReadDto dto = generator.generateRandomObject(CorrectionReadDto.class);
        dto.setUser(this.createUserReadDto());
        dto.setPublication(this.createPublicationReadDto());

        return dto;
    }

    public void inTransaction(Runnable runnable) {
        transactionTemplate.executeWithoutResult(status -> runnable.run());
    }
}