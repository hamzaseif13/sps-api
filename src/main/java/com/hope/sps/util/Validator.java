package com.hope.sps.util;

import org.springframework.stereotype.Component;

@Component
public class Validator {

    public boolean validateUserPassword(final String passwordBeforeHashing) {
        final String passwordValidationRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if (passwordBeforeHashing == null || passwordBeforeHashing.isBlank() || passwordBeforeHashing.isEmpty())
            return false;

        return passwordBeforeHashing.matches(passwordValidationRegex);
    }
}