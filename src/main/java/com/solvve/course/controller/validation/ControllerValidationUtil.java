package com.solvve.course.controller.validation;

import com.solvve.course.exception.ControllerValidationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ControllerValidationUtil {

    public <T> void validateNotEqual(T value1, T value2,
                                     String field1Value, String field2Value) {
        if (value1 != null && value1.equals(value2)) {
            throw new ControllerValidationException(String.format("Field %s=%s should not be equal to %s=%s",
                    field1Value, value1, field2Value, value2));
        }
    }
}