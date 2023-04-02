package com.hope.sps.officer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/officer")
@RequiredArgsConstructor
public class OfficerController {

    private final OfficerService officerService;


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")//todo hasRole or authority
    public ResponseEntity<List<OfficerDTO>> getAllOfficers() {
        List<OfficerDTO> officerDTOList = officerService.getAll();
        return ResponseEntity.ok(officerDTOList);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Long> registerOfficer(@RequestBody @Valid OfficerRegisterRequest request) {

        Long officerId = officerService.registerOfficer(request);
        return new ResponseEntity<>(officerId, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OfficerDTO> getOfficerById(@PathVariable Long id) {
        return new ResponseEntity<>(officerService.getOfficerById(id), HttpStatus.OK);
    }

    @PutMapping("{Id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateOfficer(
            @RequestBody @Valid
            OfficerUpdateRequest request,
            @PathVariable("Id") Long officerId) {

        officerService.updateOfficer(request, officerId);
        return new ResponseEntity<>("officer updated", HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOfficerById(@PathVariable Long id) {
        officerService.deleteOfficerById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
