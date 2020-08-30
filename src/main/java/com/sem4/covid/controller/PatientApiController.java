package com.sem4.covid.controller;

import com.sem4.covid.entity.*;
import com.sem4.covid.repository.LocationRepository;
import com.sem4.covid.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Component
public class PatientApiController {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    LocationRepository locationRepository;

    final String ROOT_URI = "https://maps.vnpost.vn/apps/covid19/api/patientapi/List";

    @Scheduled(fixedRate = 180000)
    public List<PatientApi> getAllPatientApi() {
        System.out.println("hello");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PatientApiInfo> response = restTemplate.getForEntity(ROOT_URI, PatientApiInfo.class);
        PatientApiInfo p = response.getBody();

        assert p != null;
        if (p.getCode().equals("SUCCESS")) {
            List<PatientApi> listPatient = Arrays.asList(p.getData());

            for (PatientApi item : listPatient) {
                String names = item.getName().trim();
                String[] arrayName = names.split(",");
                Calendar cal = Calendar.getInstance();

                for (int i=0; i<arrayName.length; i++) {
                    Patient patient = patientRepository.findByName(arrayName[i].trim());
                    if(patient == null) {
                        patient = new Patient();
                        patient.setPatientName(arrayName[i].trim());
                        patient.setNote(item.getNote());
                        patient.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
                        patientRepository.save(patient);
                    }
                }

                Location location = locationRepository.findByName(item.getAddress());
                if (location == null) {
                    location = new Location();
                    location.setName(item.getAddress());
                    location.setLng(item.getLng());
                    location.setLat(item.getLat());
                    location.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
                    locationRepository.save(location);
                }
            }
            return listPatient;
        }
        return null;
    }
}
