package com.hope.sps.officer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/officer")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class OfficerController {

    private final OfficerService officerService;

    @GetMapping
    public ResponseEntity<List<OfficerDTO>> getAll() {

        final var officerDTOS = officerService.getAll();
        return ResponseEntity.ok(officerDTOS);
    }

    @GetMapping("{officerId}")
    public ResponseEntity<OfficerDTO> getById(
            @PathVariable("officerId")
            @Validated @Positive
            Long officerId
    ) {

        final var officerById = officerService.getOfficerById(officerId);
        return ResponseEntity.ok(officerById);
    }


    @PostMapping
    public ResponseEntity<Long> register(
            @RequestBody @Valid
            OfficerRegisterRequest request
    ) {

        final Long officerId = officerService.registerOfficer(request);
        return new ResponseEntity<>(officerId, HttpStatus.CREATED);
    }

    @PutMapping("{Id}")
    public ResponseEntity<String> updateOfficer(
            @RequestBody @Valid
            OfficerUpdateRequest request,
            @PathVariable("Id")
            @Validated @Positive
            Long officerId) {

        officerService.updateOfficer(request, officerId);
        return new ResponseEntity<>("officer updated", HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOfficerById(
            @PathVariable
            @Validated @Positive
            Long id) {

        officerService.deleteOfficerById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
