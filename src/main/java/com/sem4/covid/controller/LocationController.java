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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 18000)
@RestController
public class LocationController {
    private final LocationRepository repository;
    private final PatientRepository patientRepository;
    private final PatientLocationRepository patientLocationRepository;

    public LocationController(LocationRepository repository, PatientRepository patientRepository, PatientLocationRepository patientLocationRepository) {
        this.repository = repository;
        this.patientRepository = patientRepository;
        this.patientLocationRepository = patientLocationRepository;
    }

    //Get All Location With Patient
    @GetMapping("/api/locations")
    public ResponseEntity<?> getAllLocations() {
        try {
            List<Location> listLocation = repository.getAllLocation();

            if (listLocation.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                List<LocationDTO> list = listLocation.stream().map(location ->{
                    LocationDTO l = new LocationDTO();
                    List<Patient> patients = patientRepository.getPatientByLocationId(location.getId());

                    List<PatientDTO> patientDTO = patients.stream().map(patient -> {
                        PatientLocation patientLocation = patientLocationRepository.findByPatientLocationId(patient.getId(), location.getId());
                        if (patientLocation != null) {
                            PatientDTO p = new PatientDTO();
                            p.setId(patient.getId());
                            p.setPatientName(patient.getPatientName());
                            p.setGender(patient.getGender());
                            p.setAge(patient.getAge());
                            p.setStatus(patient.getStatus());
                            p.setVerifyDatePatient(patient.getVerifyDatePatient());
                            p.setVerifyDate(patientLocation.getVerifyDate());
                            p.setProvince(patient.getProvince());
                            p.setCreatedAt(patient.getCreatedAt());
                            p.setUpdatedAt(patient.getUpdatedAt());
                            p.setDeletedAt(patient.getDeletedAt());

                            return p;
                        } else {
                            return null;
                        }
                    }).collect(Collectors.toList());

                    l.setId(location.getId());
                    l.setName(location.getName());
                    l.setLat(location.getLat());
                    l.setLng(location.getLng());
                    l.setProvince(location.getProvince());
                    l.setCreatedAt(location.getCreatedAt());
                    l.setUpdatedAt(location.getUpdatedAt());
                    l.setDeletedAt(location.getDeletedAt());
                    l.setPatient(patientDTO);

                    return l;
                }).collect(Collectors.toList());
                return ResponseEntity.ok(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get Location By Id
    @GetMapping("/api/location/{id}")
    ResponseEntity<?> getLocationById(@PathVariable int id) {
        try {
            Location location = repository.findByIdActive(id);
            if (location == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                LocationDTO locationDTO = new LocationDTO();
                locationDTO.setId(location.getId());
                locationDTO.setName(location.getName());
                locationDTO.setLat(location.getLat());
                locationDTO.setLng(location.getLng());
                locationDTO.setProvince(location.getProvince());
                locationDTO.setCreatedAt(location.getCreatedAt());
                locationDTO.setUpdatedAt(location.getUpdatedAt());
                locationDTO.setDeletedAt(location.getDeletedAt());

                return new ResponseEntity(locationDTO, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Create Location
    @PostMapping("/api/location")
    ResponseEntity<?> createLocation(@Valid @RequestBody Location location){
        try {
            Calendar cal = Calendar.getInstance();
            location.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
            repository.save(location);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update Location
    @PutMapping("/api/location/{id}")
    ResponseEntity<?> updateLocation(@Valid @RequestBody Location newLocation, @PathVariable int id) {
        try {
            Calendar cal = Calendar.getInstance();
            Location location = repository.findByIdActive(id);
            location.setName(newLocation.getName());
            location.setLat(newLocation.getLat());
            location.setLng(newLocation.getLng());
            location.setProvince(newLocation.getProvince());
            location.setUpdatedAt(new Timestamp(cal.getTimeInMillis()));
            repository.save(location);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete Location
    @DeleteMapping("/api/location/{id}")
    ResponseEntity<?> deleteLocation(@PathVariable int id) {
        try {
            Calendar cal = Calendar.getInstance();
            List<PatientLocation> patientLocations = patientLocationRepository.findByLocationId(id);
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
