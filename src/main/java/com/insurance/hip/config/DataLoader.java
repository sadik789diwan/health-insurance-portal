package com.insurance.hip.config;


import com.insurance.hip.entity.Patient;
import com.insurance.hip.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final PatientRepository repository;

    public DataLoader(PatientRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Patient("John Doe", 35, "Flu"));
        repository.save(new Patient("Alice Smith", 28, "Cold"));
        repository.save(new Patient("Bob Johnson", 42, "Diabetes"));
    }
}
