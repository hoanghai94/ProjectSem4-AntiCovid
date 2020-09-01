package com.sem4.covid.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "user_location")
public class UserLocation {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    private Integer id;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "verify_date")
    private java.sql.Timestamp verifyDate;

    @Column(name = "permanent")
    private Integer permanent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public java.sql.Timestamp getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(java.sql.Timestamp verifyDate) {
        this.verifyDate = verifyDate;
    }

    public Integer getPermanent() {
        return permanent;
    }

    public void setPermanent(Integer permanent) {
        this.permanent = permanent;
    }

}
