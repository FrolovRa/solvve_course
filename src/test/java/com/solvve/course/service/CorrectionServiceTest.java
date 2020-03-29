package com.solvve.course.service;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.Correction;
import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CorrectionServiceTest extends BaseTest {

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
