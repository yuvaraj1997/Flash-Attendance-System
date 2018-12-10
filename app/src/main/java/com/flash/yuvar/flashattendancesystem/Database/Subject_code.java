package com.flash.yuvar.flashattendancesystem.Database;

public class Subject_code {

    String subject_id;
    String subject_code;

    public Subject_code(){

    }

    public Subject_code(String subject_id, String subject_code) {
        this.subject_id = subject_id;
        this.subject_code = subject_code;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }
}
