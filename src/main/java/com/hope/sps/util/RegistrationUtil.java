package com.hope.sps.util;

import com.hope.sps.UserDetails.UserRepository;
import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.InvalidResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class RegistrationUtil {

    private final UserRepository userRepository;

    public void throwExceptionIfEmailExists(String email) {
        if (userRepository.existsByEmail(email))
            throw new DuplicateResourceException("email already taken");
    }

    public void throwExceptionIfPasswordInvalid(String password) {
        final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern passPattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher passMatcher = passPattern.matcher(password);
//
//        if (!passMatcher.matches())//todo
//            throw new InvalidResourceException("Invalid provided password");
    }
}
