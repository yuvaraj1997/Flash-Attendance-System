package com.flash.yuvar.flashattendancesystem.Database;

public class students_registered_class {

    String class_name;
    String classID;
    String registeredclassID;
    String secretkey;


    public students_registered_class(){

    }

    public students_registered_class(String class_name, String classID, String registeredclassID) {
        this.class_name = class_name;
        this.classID = classID;
        this.registeredclassID = registeredclassID;
    }

    public students_registered_class(String secretkey) {

        this.secretkey = secretkey;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getRegisteredclassID() {
        return registeredclassID;
    }

    public void setRegisteredclassID(String registeredclassID) {
        this.registeredclassID = registeredclassID;
    }
}
