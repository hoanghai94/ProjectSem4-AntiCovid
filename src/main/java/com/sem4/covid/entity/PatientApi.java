package com.sem4.covid.entity;

import java.sql.Timestamp;

public class PatientApi {

    private String name;

    private String address;

    private float lat;

    private float lng;

    private String patientGroup;

    private String note;

    private Timestamp verifyDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getPatientGroup() {
        return patientGroup;
    }

    public void setPatientGroup(String patientGroup) {
        this.patientGroup = patientGroup;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Timestamp verifyDate) {
        this.verifyDate = verifyDate;
    }
}
