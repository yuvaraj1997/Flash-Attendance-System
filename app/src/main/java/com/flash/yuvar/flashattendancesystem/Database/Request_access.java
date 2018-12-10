package com.flash.yuvar.flashattendancesystem.Database;

public class Request_access {

    private String class_Code;
    private String userID;
    private String request_id;
    private String classID;

    public Request_access(){

    }

    public Request_access(String class_Code, String userID, String request_id,String classID) {
        this.class_Code = class_Code;
        this.userID = userID;
        this.request_id = request_id;
        this.classID = classID;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClass_Code() {
        return class_Code;
    }

    public void setClass_Code(String class_Code) {
        this.class_Code = class_Code;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }
}

