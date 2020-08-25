package com.sem4.covid.repository;

import com.sem4.covid.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer> {

    @Query("SELECT p from Patient p where p.deletedAt IS NULL and p.id = :id")
    Patient findIdActive(@Param("id") int id);

    @Query("SELECT p from Patient p where p.deletedAt IS NULL")
    List<Patient> getAllPatientActive();

}
