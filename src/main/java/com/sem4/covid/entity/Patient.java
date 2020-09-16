package com.sem4.covid.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "patient_name")
    @NotBlank(message = "tên bệnh nhân không để trống.")
    private String patientName;

    @Column(name = "note_patient")
    private String notePatient;

    @Column(name = "verify_date_patient")
    private java.sql.Timestamp verifyDatePatient;

    @Column(name = "status")
    private String status;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private String age;

    @Column(name = "province")
    private String province;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;

    @Column(name = "updated_at")
    private java.sql.Timestamp updatedAt;

    @Column(name = "deleted_at")
    private java.sql.Timestamp deletedAt;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(name = "patient_location", joinColumns = { @JoinColumn(name = "patient_id") }, inverseJoinColumns = { @JoinColumn(name = "location_id") })
    private Set<Location> location = new HashSet<>();

    public Set<Location> getLocation() {
        return this.location;
    }

    public void setLocation(Set<Location> location) {
        this.location = location;
    }

    public Patient(){}

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

    public String getNotePatient() {
        return notePatient;
    }

    public void setNotePatient(String notePatient) {
        this.notePatient = notePatient;
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
