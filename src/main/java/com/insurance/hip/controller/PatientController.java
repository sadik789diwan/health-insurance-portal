package com.insurance.hip.controller;

import com.insurance.hip.entity.Patient;
import com.insurance.hip.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return service.getAllPatients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return service.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return service.createPatient(patient);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return service.updatePatient(id, patient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        service.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
