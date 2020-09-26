package com.sem4.covid.repository;

import com.sem4.covid.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer> {

    @Query("SELECT p from Patient p where p.deletedAt IS NULL and p.id = :id")
    Patient findIdActive(@Param("id") int id);

    @Query("SELECT p from Patient p where p.deletedAt IS NULL and p.patientName = :name")
    Patient findByName(@Param("name") String name);

    @Query("SELECT p from Patient p where p.deletedAt IS NULL order by p.id desc")
    List<Patient> getAllPatientActive();

    @Query("SELECT p from Patient p JOIN PatientLocation pt on p.id = pt.patientId where p.deletedAt IS NULL and pt.locationId = :id")
    List<Patient> getPatientByLocationId(@Param("id") int id);

    @Query("SELECT p from Patient p where p.deletedAt IS NULL and p.verifyDatePatient BETWEEN :startTime AND :endTime")
    List<Patient> getNewPatient(@Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime);
}
