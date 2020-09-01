package com.sem4.covid.entity;

public class PatientApiInfo {
    String code;
    String message;
    PatientApi[] data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PatientApi[] getData() {
        return data;
    }

    public void setData(PatientApi[] data) {
        this.data = data;
    }
}
