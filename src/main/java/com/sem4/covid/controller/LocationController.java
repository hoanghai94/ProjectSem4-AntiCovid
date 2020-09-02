package com.sem4.covid.controller;

import com.sem4.covid.entity.Location;
import com.sem4.covid.repository.LocationRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@RestController
public class LocationController {
    private final LocationRepository repository;

    public LocationController(LocationRepository repository) {
        this.repository = repository;
    }

    //Get All Locations
    @CrossOrigin
    @GetMapping("/api/locations")
    List<Location> getAllLocations() {
        return repository.getAll();
    }

    //Create Location
    @CrossOrigin
    @PostMapping("/api/location")
    Location createLocation(@RequestBody Location location){
        Calendar cal = Calendar.getInstance();
        location.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
        repository.save(location);

        return location;
    }

    //Delete Location
    @CrossOrigin
    @DeleteMapping("/api/location/{id}")
    void deleteLocation(@PathVariable int id) {
        Calendar cal = Calendar.getInstance();
        Location location = repository.findById(id);
        location.setDeletedAt(new Timestamp(cal.getTimeInMillis()));
        repository.save(location);
    }

    //Get Location By Id
    @CrossOrigin
    @GetMapping("/api/location/{id}")
    Location getLocationById(@PathVariable int id) {
        Location location = repository.findById(id);

        return location;
    }

    //Update Location
    @CrossOrigin
    @PutMapping("/api/location/{id}")
    Location updateLocation(@RequestBody Location newLocation, @PathVariable int id) {
        Calendar cal = Calendar.getInstance();
        Location location = repository.findById(id);
        location.setName(newLocation.getName());
        location.setLat(newLocation.getLat());
        location.setLng(newLocation.getLng());
        location.setUpdatedAt(new Timestamp(cal.getTimeInMillis()));

        return repository.save(location);
    }
}
