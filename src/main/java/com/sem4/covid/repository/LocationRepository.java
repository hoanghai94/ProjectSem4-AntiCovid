package com.sem4.covid.repository;

import com.sem4.covid.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("SELECT l from Location l where l.deletedAt IS NULL")
    List<Location> getAllLocation();

    @Query("SELECT l from Location l where l.deletedAt IS NULL and l.id = :id")
    Location findByIdActive(@Param("id") int id);

    @Query("SELECT l from Location l where l.name = :name")
    Location findByName(@Param("name") String name);

    @Query("SELECT l from Location l JOIN PatientLocation pt on l.id = pt.locationId where l.deletedAt IS NULL and pt.patientId = :id")
    List<Location> getLocationByPatientId(@Param("id") int id);
}
