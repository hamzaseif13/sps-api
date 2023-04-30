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
        final List<OfficerDTO> officerDTOList = officerService.getAll();

        return ResponseEntity.ok(officerDTOList);
    }

    @PostMapping
    public ResponseEntity<Long> register(
            @RequestBody
            @Valid
            OfficerRegisterRequest request
    ) {

        final Long officerId = officerService.registerOfficer(request);

        return new ResponseEntity<>(officerId, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<OfficerDTO> getById(
            @PathVariable
            @Validated
            @Positive
            Long id) {

        final OfficerDTO officerById = officerService.getOfficerById(id);

        return new ResponseEntity<>(officerById, HttpStatus.OK);
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
