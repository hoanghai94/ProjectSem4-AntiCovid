package com.sem4.covid.controller;

import com.sem4.covid.entity.*;
import com.sem4.covid.repository.LocationRepository;
import com.sem4.covid.repository.PatientLocationRepository;
import com.sem4.covid.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class PatientApiController {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    PatientLocationRepository patientLocationRepository;

    final String ROOT_URI = "https://maps.vnpost.vn/apps/covid19/api/patientapi/List";

    final String ROOT_URI_NEW = "http://anticovidaptech.herokuapp.com/patients";

    public List<PatientApi> getAllPatientApi() {
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

                Location location = locationRepository.findByName(item.getAddress());
                if (location == null) {
                    location = new Location();
                    location.setName(item.getAddress());
                    location.setLng(item.getLng());
                    location.setLat(item.getLat());
                    location.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
                    location = locationRepository.save(location);
                }

                for (int i=0; i<arrayName.length; i++) {
                    Patient patient = patientRepository.findByName(arrayName[i].trim());
                    if(patient == null) {
                        String[] patientName = arrayName[i].split("-");
                        int patientId = Integer.parseInt(patientName[1].trim());
                        patient = new Patient();
                        patient.setId(patientId);
                        patient.setPatientName(arrayName[i].trim());
                        patient.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
                        patient = patientRepository.save(patient);

                        PatientLocation patientLocation = new PatientLocation();
                        patientLocation.setLocationId(location.getId());
                        patientLocation.setPatientId(patient.getId());
                        patientLocation.setNote(item.getNote());
                        patientLocation.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
                        if (item.getVerifyDate().after(Timestamp.valueOf("2019-10-01 18:55:00"))) {
                            patientLocation.setVerifyDate(item.getVerifyDate());
                        } else {
                            patientLocation.setVerifyDate(null);
                        }
                        patientLocationRepository.save(patientLocation);
                    } else {
                        PatientLocation patientLocation = patientLocationRepository.findByPatientLocationId(patient.getId(), location.getId());
                        if (patientLocation == null) {
                            PatientLocation newPatientLocation = new PatientLocation();
                            newPatientLocation.setPatientId(patient.getId());
                            newPatientLocation.setLocationId(location.getId());
                            newPatientLocation.setNote(item.getNote());
                            newPatientLocation.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
                            if (item.getVerifyDate().after(Timestamp.valueOf("2019-10-01 18:55:00"))) {
                                newPatientLocation.setVerifyDate(item.getVerifyDate());
                            } else {
                                newPatientLocation.setVerifyDate(null);
                            }
                            patientLocationRepository.save(newPatientLocation);
                        }
                    }
                }
            }
            return listPatient;
        }
        return null;
    }

    @Scheduled(cron = "0 0 22 * * ?", zone = "Asia/Ho_Chi_Minh")
    public List<PatientApi> getNewPatientApi() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());
        final String ROOT_URI_DATE = "http://anticovidaptech.herokuapp.com/patients?date=" + currentDate;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PatientApiInfo> response = restTemplate.getForEntity(ROOT_URI_DATE, PatientApiInfo.class);
        PatientApiInfo p = response.getBody();

        assert p != null;
        if (p.getCode().equals("SUCCESS")) {
            List<PatientApi> listPatient = Arrays.asList(p.getData());
            if (listPatient.isEmpty()) {
                return null;
            } else {
                for (PatientApi item : listPatient) {
                    Calendar cal = Calendar.getInstance();
                    Patient patient = patientRepository.findByName(item.getPatientName());
                    if(patient == null) {
                        patient = new Patient();
                        patient.setId(item.getId());
                        patient.setPatientName(item.getPatientName());
                        patient.setGender(item.getGender());
                        patient.setAge(item.getAge());
                        patient.setStatus(item.getStatus());
                        patient.setProvince(item.getProvince());
                        patient.setNotePatient(item.getNote());
                        patient.setVerifyDatePatient(item.getVerifyDate());
                        patient.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
                        patientRepository.save(patient);
                    } else {
                        patient.setId(item.getId());
                        patient.setPatientName(item.getPatientName());
                        patient.setGender(item.getGender());
                        patient.setAge(item.getAge());
                        patient.setStatus(item.getStatus());
                        patient.setProvince(item.getProvince());
                        patient.setNotePatient(item.getNote());
                        patient.setVerifyDatePatient(item.getVerifyDate());
                        patient.setUpdatedAt(new Timestamp(cal.getTimeInMillis()));
                        patientRepository.save(patient);
                    }
                }
                return listPatient;
            }
        }
        return null;
    }
}
