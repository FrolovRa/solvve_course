package com.solvve.course.service;

import com.solvve.course.domain.Correction;
import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.CorrectionRepository;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {
        "delete from correction",
        "delete from publication",
        "delete from user"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CorrectionServiceTest {

    @Autowired
    private CorrectionRepository correctionRepository;

    @Autowired
    private TestUtils utils;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private CorrectionService correctionService;

    @Test
    public void testGetCorrection() {
        Correction correction = utils.getCorrectionFromDb();
        CorrectionReadDto actualCorrection = translationService.translate(correction, CorrectionReadDto.class);

        CorrectionReadDto correctionReadDto = correctionService.getCorrection(correction.getId());

        assertThat(actualCorrection).isEqualToComparingFieldByField(correctionReadDto);
    }

    @Test
    public void testGetAllCorrections() {
        Correction correction = utils.getCorrectionFromDb();
        Correction secondCorrection = utils.getCorrectionFromDb();
        Correction thirdCorrection = utils.getCorrectionFromDb();

        List<CorrectionReadDto> result = correctionService.getAllCorrections();

        assertEquals(3, result.size());
        assertThat(result).extracting("id")
                .containsExactlyInAnyOrder(correction.getId(), secondCorrection.getId(), thirdCorrection.getId());
    }

    @Test
    public void testDeleteCorrection() {
        Correction correction = utils.getCorrectionFromDb();

        correctionService.deleteCorrection(correction.getId());

        assertFalse(correctionRepository.existsById(correction.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        correctionService.deleteCorrection(UUID.randomUUID());
    }
}
