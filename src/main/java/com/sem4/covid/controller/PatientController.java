package com.sem4.covid.controller;

import com.sem4.covid.dto.LocationDTO;
import com.sem4.covid.dto.PatientDTO;
import com.sem4.covid.entity.Location;
import com.sem4.covid.entity.Patient;
import com.sem4.covid.entity.PatientLocation;
import com.sem4.covid.repository.LocationRepository;
import com.sem4.covid.repository.PatientLocationRepository;
import com.sem4.covid.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class PatientController {
    private final PatientRepository repository;
    private final LocationRepository locationRepository;
    private final PatientLocationRepository patientLocationRepository;

    public PatientController(PatientRepository repository, LocationRepository locationRepository, PatientLocationRepository patientLocationRepository, PatientLocationRepository patientLocationRepository1) {
        this.repository = repository;
        this.locationRepository = locationRepository;
        this.patientLocationRepository = patientLocationRepository1;
    }

    //Get All Patient With Location
    @CrossOrigin
    @GetMapping("/api/patients")
    public ResponseEntity<?> getAllPatients() {
        try {
            List<Patient> listPatient = repository.getAllPatientActive();

            if (listPatient.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                List<PatientDTO> list = listPatient.stream().map(patient ->{
                    PatientDTO p = new PatientDTO();

                    List<Location> locations = locationRepository.getLocationByPatientId(patient.getId());
                    List<LocationDTO> locationDTO = locations.stream().map(location -> {
                        PatientLocation patientLocation = patientLocationRepository.findByPatientLocationId(patient.getId(), location.getId());

                        LocationDTO l = new LocationDTO();
                        l.setId(location.getId());
                        l.setName(location.getName());
                        l.setLat(location.getLat());
                        l.setLng(location.getLng());
                        l.setProvince(location.getProvince());
                        l.setVerifyDate(patientLocation.getVerifyDate());
                        l.setCreatedAt(location.getCreatedAt());
                        l.setUpdatedAt(location.getUpdatedAt());
                        l.setDeletedAt(location.getDeletedAt());

                        return l;
                    }).collect(Collectors.toList());

                    p.setId(patient.getId());
                    p.setPatientName(patient.getPatientName());
                    p.setNote(patient.getNote());
                    p.setAge(patient.getAge());
                    p.setGender(patient.getGender());
                    p.setStatus(patient.getStatus());
                    p.setProvince(patient.getProvince());
                    p.setVerifyDatePatient(patient.getVerifyDatePatient());
                    p.setCreatedAt(patient.getCreatedAt());
                    p.setUpdatedAt(patient.getUpdatedAt());
                    p.setDeletedAt(patient.getDeletedAt());
                    p.setLocation(locationDTO);

                    return p;
                }).collect(Collectors.toList());
                return ResponseEntity.ok(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get One Patient
    @CrossOrigin
    @GetMapping("/api/patient/{id}")
    ResponseEntity<?> getUPatientById(@PathVariable int id) {
        try {
            Patient patient = repository.findIdActive(id);
            if (patient == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setId(patient.getId());
                patientDTO.setPatientName(patient.getPatientName());
                patientDTO.setGender(patient.getGender());
                patientDTO.setAge(patient.getAge());
                patientDTO.setStatus(patient.getStatus());
                patientDTO.setNote(patient.getNote());
                patientDTO.setProvince(patient.getProvince());
                patientDTO.setVerifyDatePatient(patient.getVerifyDatePatient());
                patientDTO.setCreatedAt(patient.getCreatedAt());
                patientDTO.setUpdatedAt(patient.getUpdatedAt());
                patientDTO.setDeletedAt(patient.getDeletedAt());

                return new ResponseEntity(patientDTO, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Create Patient
    @CrossOrigin
    @PostMapping("/api/patient")
    ResponseEntity<?> createPatient(@RequestBody Patient patient){
        try {
            Calendar cal = Calendar.getInstance();
            patient.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
            repository.save(patient);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update Patient
    @CrossOrigin
    @PutMapping("/api/patient/{id}")
    ResponseEntity<?> updatePatient(@RequestBody Patient newPatient, @PathVariable int id) {
        try {
            Calendar cal = Calendar.getInstance();

            Patient patient = repository.findIdActive(id);
            patient.setPatientName(newPatient.getPatientName());
            patient.setNote(newPatient.getNote());
            patient.setVerifyDatePatient(newPatient.getVerifyDatePatient());
            patient.setGender(newPatient.getGender());
            patient.setAge(newPatient.getAge());
            patient.setStatus(newPatient.getStatus());
            patient.setProvince(newPatient.getProvince());
            patient.setUpdatedAt(new Timestamp(cal.getTimeInMillis()));
            repository.save(patient);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete Patient
    @CrossOrigin
    @DeleteMapping("/api/patient/{id}")
    ResponseEntity<?> deletePatient(@PathVariable int id) {
        try {
            Calendar cal = Calendar.getInstance();
            Patient patient = repository.findById(id).get();;
            patient.setDeletedAt(new Timestamp(cal.getTimeInMillis()));
            repository.save(patient);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
