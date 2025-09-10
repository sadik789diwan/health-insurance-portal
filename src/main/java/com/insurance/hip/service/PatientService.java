package com.insurance.hip.service;


import com.insurance.hip.entity.Patient;
import com.insurance.hip.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public List<Patient> getAllPatients() {
        return repository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return repository.findById(id);
    }

    public Patient createPatient(Patient patient) {
        return repository.save(patient);
    }

    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id " + id));
        patient.setName(patientDetails.getName());
        patient.setAge(patientDetails.getAge());
        patient.setDisease(patientDetails.getDisease());
        return repository.save(patient);
    }

    public void deletePatient(Long id) {
        repository.deleteById(id);
    }
}
