package com.flash.yuvar.flashattendancesystem.Database;

public class student_registered_list {

    private String uID;
    private String name;
    private Integer count;
    private String EndDate;

    public student_registered_list(){

    }

    public student_registered_list(Integer count) {
        this.count = count;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public student_registered_list(String uID, String name) {
        this.uID = uID;
        this.name = name;
    }

    public student_registered_list(String uID, String name,Integer count) {
        this.uID = uID;
        this.name = name;
        this.count = count;
    }

    public student_registered_list(String uID, String name,String EndDate) {
        this.uID = uID;
        this.name = name;

        this.EndDate = EndDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
