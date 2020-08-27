package com.sem4.covid.controller;

import com.sem4.covid.entity.Patient;
import com.sem4.covid.repository.PatientRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@RestController
public class PatientController {
    private final PatientRepository repository;

    public PatientController(PatientRepository repository) {
        this.repository = repository;
    }

    //Get All Patients
    @CrossOrigin
    @GetMapping("/api/patients")
    List<Patient> getAllPatients() {
        return repository.getAllPatientActive();
    }

    //Create Patient
    @CrossOrigin
    @PostMapping("/api/patient")
    Patient createPatient(@RequestBody Patient patient){
        Calendar cal = Calendar.getInstance();
        patient.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
        repository.save(patient);

        return patient;
    }

    //Get One Patients
    @CrossOrigin
    @GetMapping("/api/patient/{id}")
    Patient getUPatientById(@PathVariable int id) {
        Patient patient = repository.findIdActive(id);

        return patient ;
    }

    //Update Patient
    @CrossOrigin
    @PutMapping("/api/patient/{id}")
    Patient updatePatient(@RequestBody Patient newPatient, @PathVariable int id) {
        Calendar cal = Calendar.getInstance();
        Patient patient = repository.findIdActive(id);
        patient.setPatientName(newPatient.getPatientName());
        patient.setNote(newPatient.getNote());
        patient.setUpdatedAt(new Timestamp(cal.getTimeInMillis()));

        return repository.save(patient);
    }

    //Delete Patient
    @CrossOrigin
    @DeleteMapping("/api/patient/{id}")
    void deletePatient(@PathVariable int id) {
        Calendar cal = Calendar.getInstance();
        Patient patient = repository.findById(id).get();;
        patient.setDeletedAt(new Timestamp(cal.getTimeInMillis()));
        repository.save(patient);
    }
}
