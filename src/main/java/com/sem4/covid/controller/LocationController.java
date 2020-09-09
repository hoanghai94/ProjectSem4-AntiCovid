package com.sem4.covid.controller;

import com.sem4.covid.dto.LocationDTO;
import com.sem4.covid.dto.PatientDTO;
import com.sem4.covid.entity.Location;
import com.sem4.covid.entity.Patient;
import com.sem4.covid.repository.LocationRepository;
import com.sem4.covid.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LocationController {
    private final LocationRepository repository;
    private final PatientRepository patientRepository;

    public LocationController(LocationRepository repository, PatientRepository patientRepository) {
        this.repository = repository;
        this.patientRepository = patientRepository;
    }

    //Get All Locations
//    @CrossOrigin
//    @GetMapping("/api/locations")
//    public ResponseEntity<?> getAllLocations() {
//        try {
//            List<Location> listLocation = repository.getAllLocation();
//
//            if (listLocation.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            } else {
//                List<LocationDTO> list = listLocation.stream().map(location ->{
//                    LocationDTO l = new LocationDTO();
//
//                    List<Patient> patients = patientRepository.getPatientByLocationId(location.getId());
//                    List<PatientDTO> patientDTO = patients.stream().map(patient -> {
//                        PatientDTO p = new PatientDTO();
//                        p.setId(patient.getId());
//                        p.setPatientName(patient.getPatientName());
//                        p.setGender(patient.getGender());
//                        p.setAge(patient.getAge());
//                        p.setStatus(patient.getStatus());
//                        p.setVerifyDatePatient(patient.getVerifyDatePatient());
//                        p.setProvince(patient.getProvince());
//                        p.setCreatedAt(patient.getCreatedAt());
//                        p.setUpdatedAt(patient.getUpdatedAt());
//                        p.setDeletedAt(patient.getDeletedAt());
//
//                        return p;
//                    }).collect(Collectors.toList());
//
//                    l.setId(location.getId());
//                    l.setName(location.getName());
//                    l.setLat(location.getLat());
//                    l.setLng(location.getLng());
//                    l.setProvince(location.getProvince());
//                    l.setCreatedAt(location.getCreatedAt());
//                    l.setUpdatedAt(location.getUpdatedAt());
//                    l.setDeletedAt(location.getDeletedAt());
//                    l.setPatient(patientDTO);
//
//                    return l;
//                }).collect(Collectors.toList());
//                return ResponseEntity.ok(list);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    //Get Location By Id
//    @CrossOrigin
//    @GetMapping("/api/location/{id}")
//    Location getLocationById(@PathVariable int id) {
//        Location location = repository.findByIdActive(id);
//
//        return location;
//    }

    //Create Location
    @CrossOrigin
    @PostMapping("/api/location")
    ResponseEntity<?> createLocation(@RequestBody Location location){
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
    @CrossOrigin
    @PutMapping("/api/location/{id}")
    ResponseEntity<?> updateLocation(@RequestBody Location newLocation, @PathVariable int id) {
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
    @CrossOrigin
    @DeleteMapping("/api/location/{id}")
    ResponseEntity<?> deleteLocation(@PathVariable int id) {
        try {
            Calendar cal = Calendar.getInstance();
            Location location = repository.findByIdActive(id);
            location.setDeletedAt(new Timestamp(cal.getTimeInMillis()));
            repository.save(location);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
