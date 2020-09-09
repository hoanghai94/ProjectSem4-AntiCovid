package com.sem4.covid.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PatientDTO {
    private Integer id;

    private String patientName;

    private String note;

    private java.sql.Timestamp verifyDatePatient;

    private String status;

    private String gender;

    private String age;

    private String province;

    private java.sql.Timestamp createdAt;

    private java.sql.Timestamp updatedAt;

    private java.sql.Timestamp deletedAt;

    private List<LocationDTO> location = new ArrayList<LocationDTO>();

    public List<LocationDTO> getLocation() {
        return location;
    }

    public void setLocation(List<LocationDTO> location) {
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Timestamp getVerifyDatePatient() {
        return verifyDatePatient;
    }

    public void setVerifyDatePatient(Timestamp verifyDatePatient) {
        this.verifyDatePatient = verifyDatePatient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
