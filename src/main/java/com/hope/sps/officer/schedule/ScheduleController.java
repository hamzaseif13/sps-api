package com.hope.sps.officer.schedule;

import com.hope.sps.user_information.UserInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("logged_in")
    public ResponseEntity<ScheduleDTO> getLoggedInOfficerSchedule(
            @AuthenticationPrincipal UserInformation loggedInOfficer
    ) {
        final Optional<ScheduleDTO> scheduleDTO = scheduleService
                .getCurrentScheduleByEmail(loggedInOfficer.getEmail());

        return scheduleDTO.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }
}
