package com.sem4.covid.repository;

import com.sem4.covid.entity.PatientLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientLocationRepository extends JpaRepository<PatientLocation, Integer> { }
