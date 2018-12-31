package com.flash.yuvar.flashattendancesystem.Database;

public class attendance_list_push_qr {


    String dateandtime;
    String attendance_list_id;
    String uri;

    public attendance_list_push_qr(){

    }


    public attendance_list_push_qr(String dateandtime, String attendance_list_id, String uri) {
        this.dateandtime = dateandtime;
        this.attendance_list_id = attendance_list_id;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(String dateandtime) {
        this.dateandtime = dateandtime;
    }

    public String getAttendance_list_id() {
        return attendance_list_id;
    }

    public void setAttendance_list_id(String attendance_list_id) {
        this.attendance_list_id = attendance_list_id;
    }
}
