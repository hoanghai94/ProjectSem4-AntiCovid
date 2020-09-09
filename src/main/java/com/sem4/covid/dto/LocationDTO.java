package com.sem4.covid.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LocationDTO {
    private Integer id;

    private String name;

    private float lat;

    private float lng;

    private String province;

    private java.sql.Timestamp verifyDate;

    private java.sql.Timestamp createdAt;

    private java.sql.Timestamp updatedAt;

    private java.sql.Timestamp deletedAt;

    private List<PatientDTO> patient = new ArrayList<PatientDTO>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<PatientDTO> getPatient() {
        return patient;
    }

    public void setPatient(List<PatientDTO> patient) {
        this.patient = patient;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Timestamp getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Timestamp verifyDate) {
        this.verifyDate = verifyDate;
    }
}
