package com.hope.sps.util;

import com.hope.sps.exception.InvalidResourceProvidedException;

import java.util.regex.PatternSyntaxException;

public class Validator {

    private static final String passwordValidationRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    /**
     * @throws InvalidResourceProvidedException if the passwordBeforeHashing not satisfying the validationRegex
     */
    public static void validateUserPassword(final String passwordBeforeHashing) {
        if (!passwordBeforeHashing.matches(Validator.passwordValidationRegex))
            throw new InvalidResourceProvidedException("invalid password");
    }
}
