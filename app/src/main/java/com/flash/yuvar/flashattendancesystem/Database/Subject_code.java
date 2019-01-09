package com.flash.yuvar.flashattendancesystem.Database;

public class Subject_code {

    private String subject_id;
    private String subject_code;
    private String lecture_name;
    private Integer password;

    public Subject_code(){

    }

    public Subject_code(String subject_id, String subject_code,String lecture_name,Integer password) {
        this.subject_id = subject_id;
        this.subject_code = subject_code;
        this.lecture_name = lecture_name;
        this.password = password;
    }
    public Subject_code(String subject_code) {

        this.subject_code = subject_code;

    }



    public Integer getPassword() {
        return password;
    }

    public void setPassword(Integer password) {
        this.password = password;
    }

    public String getLecture_name() {
        return lecture_name;
    }

    public void setLecture_name(String lecture_name) {
        this.lecture_name = lecture_name;
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
