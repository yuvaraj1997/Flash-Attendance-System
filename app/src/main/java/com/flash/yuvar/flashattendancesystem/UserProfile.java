package com.flash.yuvar.flashattendancesystem;

public class UserProfile {

    public String student_id;
    public String course;
    public String name;
    public String contact_number;
    public String email;
    public String gender;
    public String sem;
    public String type;
    public String year;



    public UserProfile(){
    }

    public UserProfile(String student_id, String course, String name, String contact_number, String email, String gender, String sem, String type, String year) {
        this.student_id = student_id;
        this.course = course;
        this.name = name;
        this.contact_number = contact_number;
        this.email = email;
        this.gender = gender;
        this.sem = sem;
        this.type = type;
        this.year = year;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUserId() {
        return student_id;
    }

    public void setUserId(String student_Id) {
        this.student_id = student_id;
    }

    public String getUserCourse() {
        return course;
    }

    public void setUserCourse(String course) {
        this.course = course;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }
}
