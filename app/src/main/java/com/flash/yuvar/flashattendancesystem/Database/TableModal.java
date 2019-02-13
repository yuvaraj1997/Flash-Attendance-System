package com.flash.yuvar.flashattendancesystem.Database;

public class TableModal {

    private String Date1, Date2;
    private String Name;
    private int rank;


    public TableModal(String date1, String date2, String name, int rank) {
        Date1 = date1;
        Date2 = date2;
        Name = name;
        this.rank = rank;
    }

    public String getDate1() {
        return Date1;
    }

    public void setDate1(String date1) {
        Date1 = date1;
    }

    public String getDate2() {
        return Date2;
    }

    public void setDate2(String date2) {
        Date2 = date2;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
