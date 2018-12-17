package com.flash.yuvar.flashattendancesystem.Database;

public class attendance_list_push_qr {


    String dateandtime;
    String attendance_list_id;

    public attendance_list_push_qr(){

    }

    public attendance_list_push_qr(String dateandtime) {
        this.dateandtime = dateandtime;

    }

    public attendance_list_push_qr(String dateandtime, String attendance_list_id) {
        this.dateandtime = dateandtime;
        this.attendance_list_id = attendance_list_id;
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
