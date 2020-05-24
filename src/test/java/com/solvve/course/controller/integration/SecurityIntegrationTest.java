package com.solvve.course.controller.integration;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.Correction;
import com.solvve.course.domain.Principal;
import com.solvve.course.domain.PrincipalRole;
import com.solvve.course.domain.User;
import com.solvve.course.domain.constant.Role;
import com.solvve.course.dto.PageResult;
import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.repository.PrincipalRepository;
import org.assertj.core.api.Assertions;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@ActiveProfiles({"test", "integration-test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SecurityIntegrationTest extends BaseTest {

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testHealthNoSecurity() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.getForEntity("http://localhost:8080/health", Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetMoviesNoSecurity() {
        RestTemplate restTemplate = new RestTemplate();
        Assertions.assertThatThrownBy(() -> restTemplate.exchange(
                "http://localhost:8080/api/v1/movies", HttpMethod.GET, HttpEntity.EMPTY,
                new ParameterizedTypeReference<Object>() {
                })).isInstanceOf(HttpClientErrorException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testGetMoviesNoSession() {
        final String email = "test@mail.reom";
        final String password = "pass1234";

        Principal principal = new Principal();
        principal.setName("Bob");
        principal.setEmail(email);
        principal.setPassword(passwordEncoder.encode(password));

        principalRepository.save(principal);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<PageResult<List<MovieReadDto>>> response = restTemplate.exchange(
                "http://localhost:8080/api/v1/movies", HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNull(response.getHeaders().get("Set-Cookie"));
    }

    @Test
    public void testGetMoviesWrongPassword() {
        final String email = "test@mail.reom";
        final String password = "pass1234";
        final String wrongPassword = "wrong";

        Principal principal = new Principal();
        principal.setName("Bob");
        principal.setEmail(email);
        principal.setPassword(passwordEncoder.encode(password));

        principalRepository.save(principal);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, wrongPassword));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        Assertions.assertThatThrownBy(() -> restTemplate.exchange(
                "http://localhost:8080/api/v1/movies", HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<Object>() {
                })).isInstanceOf(HttpClientErrorException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testGetMoviesWrongEmail() {
        final String email = "test@mail.reom";
        final String password = "pass1234";
        final String wrongEmail = "wrong";

        Principal principal = new Principal();
        principal.setName("Bob");
        principal.setEmail(email);
        principal.setPassword(passwordEncoder.encode(password));

        principalRepository.save(principal);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(wrongEmail, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        Assertions.assertThatThrownBy(() -> restTemplate.exchange(
                "http://localhost:8080/api/v1/movies", HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<Object>() {
                })).isInstanceOf(HttpClientErrorException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testGetAllCorrections() {
        final String email = "test@mail.reom";
        final String password = "pass1234";

        Principal principal = new Principal();
        principal.setName("Bob");
        principal.setEmail(email);
        principal.setPassword(passwordEncoder.encode(password));

        principalRepository.save(principal);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        Assertions.assertThatThrownBy(() -> restTemplate.exchange(
                "http://localhost:8080/api/v1/corrections", HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<Object>() {
                })).isInstanceOf(HttpClientErrorException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void testGetAllCorrectionsWithAdminRole() {
        final String email = "test@mail.reom";
        final String password = "pass1234";

        PrincipalRole adminRole = principalRoleRepository.getByRole(Role.ADMIN);

        Principal principal = new Principal();
        principal.setName("Bob");
        principal.setEmail(email);
        principal.setRoles(List.of(adminRole));
        principal.setPassword(passwordEncoder.encode(password));

        principalRepository.save(principal);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<List<CorrectionReadDto>> response = restTemplate.exchange(
                "http://localhost:8080/api/v1/corrections", HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllCorrectionsWithManagerRole() {
        final String email = "test@mail.reom";
        final String password = "pass1234";

        PrincipalRole managerRole = principalRoleRepository.getByRole(Role.CONTENT_MANAGER);

        Principal principal = new Principal();
        principal.setName("Bob");
        principal.setEmail(email);
        principal.setRoles(List.of(managerRole));
        principal.setPassword(passwordEncoder.encode(password));

        principalRepository.save(principal);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        Assertions.assertThatThrownBy(() -> restTemplate.exchange(
                "http://localhost:8080/api/v1/corrections", HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<Object>() {
                })).isInstanceOf(HttpClientErrorException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void testGetAllCorrectionsForUserWithUserRoleWrongId() {
        final String email = "test@mail.reom";
        final String password = "pass1234";

        PrincipalRole userRole = principalRoleRepository.getByRole(Role.USER);

        Principal principal = new Principal();
        principal.setName("Bob");
        principal.setEmail(email);
        principal.setRoles(List.of(userRole));
        principal.setPassword(passwordEncoder.encode(password));
        principalRepository.save(principal);

        Principal secondPrincipal = new Principal();
        secondPrincipal.setName("Bob");
        secondPrincipal.setEmail("email@s.q");
        secondPrincipal.setRoles(List.of(userRole));
        secondPrincipal.setPassword(passwordEncoder.encode("password"));
        secondPrincipal = principalRepository.save(secondPrincipal);

        User user = new User();
        user.setPrincipal(secondPrincipal);
        user = userRepository.save(user);

        Correction correction = utils.getCorrectionFromDb();
        correction.setUser(user);
        correction.setPublication(utils.getPublicationFromDb());

        correctionRepository.save(correction);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        Assertions.assertThatThrownBy(() -> restTemplate.exchange(
                "http://localhost:8080/api/v1/users/" + UUID.randomUUID() + "/corrections",
                HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<CorrectionReadDto>>() {
                })).isInstanceOf(HttpClientErrorException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    private String getBasicAuthorizationHeaderValue(String username, String password) {
        return "Basic " + new String(Base64.encode(String.format("%s:%s", username, password).getBytes()));
    }
}