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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 18000)
@RestController
public class PatientController {
    private final PatientRepository repository;
    private final LocationRepository locationRepository;
    private final PatientLocationRepository patientLocationRepository;

    public PatientController(PatientRepository repository, LocationRepository locationRepository, PatientLocationRepository patientLocationRepository) {
        this.repository = repository;
        this.locationRepository = locationRepository;
        this.patientLocationRepository = patientLocationRepository;
    }

    //Get All Patient With Location
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
                        if (patientLocation != null) {
                            LocationDTO l = new LocationDTO();
                            l.setId(location.getId());
                            l.setName(location.getName());
                            l.setLat(location.getLat());
                            l.setLng(location.getLng());
                            l.setProvince(location.getProvince());
                            l.setVerifyDate(patientLocation.getVerifyDate());
                            l.setVerifyDatePatient(patient.getVerifyDatePatient());
                            l.setCreatedAt(location.getCreatedAt());
                            l.setUpdatedAt(location.getUpdatedAt());
                            l.setDeletedAt(location.getDeletedAt());

                            return l;
                        } else {
                            return null;
                        }
                    }).collect(Collectors.toList());

                    p.setId(patient.getId());
                    p.setPatientName(patient.getPatientName());
                    p.setNotePatient(patient.getNotePatient());
                    p.setAge(patient.getAge());
                    p.setGender(patient.getGender());
                    p.setStatus(patient.getStatus());
                    p.setProvince(patient.getProvince());
                    p.setVerifyDate(patient.getVerifyDatePatient());
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
    @GetMapping("/api/patient/{id}")
    ResponseEntity<?> getPatientById(@PathVariable int id) {
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
                patientDTO.setNotePatient(patient.getNotePatient());
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
    @PostMapping("/api/patient")
    ResponseEntity<?> createPatient(@Valid @RequestBody Patient patient){
        try {
            if (repository.findByName(patient.getPatientName()) != null){
                return new ResponseEntity<String>(
                        String.format("tên bệnh nhân đã tồn tại."), HttpStatus.BAD_REQUEST);
            }

            Calendar cal = Calendar.getInstance();
            patient.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
            repository.save(patient);

            return new ResponseEntity<Patient>(patient, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update Patient
    @PutMapping("/api/patient/{id}")
    ResponseEntity<?> updatePatient(@Valid @RequestBody Patient newPatient, @PathVariable int id) {
        try {
            Calendar cal = Calendar.getInstance();

            Patient patient = repository.findIdActive(id);
            patient.setPatientName(newPatient.getPatientName());
            patient.setNotePatient(newPatient.getNotePatient());
            patient.setVerifyDatePatient(newPatient.getVerifyDatePatient());
            patient.setGender(newPatient.getGender());
            patient.setAge(newPatient.getAge());
            patient.setStatus(newPatient.getStatus());
            patient.setProvince(newPatient.getProvince());
            patient.setUpdatedAt(new Timestamp(cal.getTimeInMillis()));
            repository.save(patient);

            return new ResponseEntity<Patient>(patient, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete Patient
    @DeleteMapping("/api/patient/{id}")
    ResponseEntity<?> deletePatient(@PathVariable int id) {
        try {
            Calendar cal = Calendar.getInstance();
            List<PatientLocation> patientLocations = patientLocationRepository.findByPatientId(id);
            if (!patientLocations.isEmpty()) {
                for (PatientLocation item : patientLocations) {
                    item.setDeletedAt(new Timestamp(cal.getTimeInMillis()));
                    patientLocationRepository.save(item);
                }
            }
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get All Table Patient_Location
    @GetMapping("/api/patient-location")
    ResponseEntity<?> getAllPatientLocation() {
        try {
            List<PatientLocation> listPatientLocation = patientLocationRepository.getAllPatientLocation();
            return new ResponseEntity(listPatientLocation, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Add patient to location
    @CrossOrigin
    @PostMapping("/api/patientlocation")
    ResponseEntity<?> addPatientLocation(@RequestBody PatientLocation patientLocation) {
        try {
            patientLocationRepository.save(patientLocation);

            return new ResponseEntity<PatientLocation>(
                    patientLocation, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
