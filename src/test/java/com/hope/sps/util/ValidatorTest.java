package com.hope.sps.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ValidatorTest {

    private final Validator validator = new Validator();

    @Test
    void testValidateUserPassword_WithValidPassword_ShouldReturnTrue() {
        // Prepare
        final String validPassword = "Password@123!";

        // Execute
        boolean isValid = validator.validateUserPassword(validPassword);

        // Assert
        assertThat(isValid).isTrue();
    }

    @Test
    void testValidateUserPassword_WithInvalidPassword_ShouldReturnFalse() {
        // Prepare
        final String invalidPassword = "weakpwd";

        // Execute
        boolean isValid = validator.validateUserPassword(invalidPassword);

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    void testValidateUserPassword_WithNullPassword_ShouldReturnFalse() {
        // Prepare
        final String nullPassword = null;

        // Execute
        boolean isValid = validator.validateUserPassword(nullPassword);

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    void testValidateUserPassword_WithEmptyPassword_ShouldReturnFalse() {
        // Prepare
        final String emptyPassword = "";

        // Execute
        boolean isValid = validator.validateUserPassword(emptyPassword);

        // Assert
        assertThat(isValid).isFalse();
    }
}
