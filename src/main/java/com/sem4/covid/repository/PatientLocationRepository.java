package com.sem4.covid.repository;

import com.sem4.covid.entity.PatientLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientLocationRepository extends JpaRepository<PatientLocation, Integer> {

    @Query("SELECT p from PatientLocation p where p.deletedAt IS NULL")
    List<PatientLocation> getAllPatientLocation();

    @Query("SELECT p from PatientLocation p where p.deletedAt IS NULL and p.patientId = :patientId and p.locationId = :locationId")
    PatientLocation findByPatientLocationId(@Param("patientId") Integer patientId, @Param("locationId") Integer locationId);

    @Query("SELECT p from PatientLocation p where p.deletedAt IS NULL and p.patientId = :patientId")
    List<PatientLocation> findByPatientId(@Param("patientId") Integer patientId);

    @Query("SELECT p from PatientLocation p where p.deletedAt IS NULL and p.locationId = :locationId")
    List<PatientLocation> findByLocationId(@Param("locationId") Integer locationId);
}
