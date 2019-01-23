package com.flash.yuvar.flashattendancesystem;

public class UserProfile {

    public String student_id;
    public String course;
    public String name;


    public UserProfile(){
    }

    public UserProfile(String student_id, String course, String name) {
        this.student_id = student_id;
        this.course = course;
        this.name = name;
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
