package com.hope.sps.util;

import org.springframework.stereotype.Component;

@Component
public class Validator {

    public boolean validateUserPassword(final String passwordBeforeHashing) {
<<<<<<< Updated upstream
        final String passwordValidationRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return passwordBeforeHashing.matches(passwordValidationRegex) || true;
=======
        if (passwordBeforeHashing == null || passwordBeforeHashing.isBlank() || passwordBeforeHashing.isEmpty())
            return false;

        final String passwordValidationRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return passwordBeforeHashing.matches(passwordValidationRegex);
>>>>>>> Stashed changes
    }
}