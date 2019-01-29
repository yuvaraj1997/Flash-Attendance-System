package com.flash.yuvar.flashattendancesystem.Database;

public class admin_profile_detail {

    private String email;
    private String name;
    private String pass;
    private String type;

    public admin_profile_detail(){

    }

    public admin_profile_detail(String email, String name, String pass, String type) {
        this.email = email;
        this.name = name;
        this.pass = pass;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
