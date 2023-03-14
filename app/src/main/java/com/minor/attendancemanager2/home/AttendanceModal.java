package com.minor.attendancemanager2.home;

public class AttendanceModal {
    public AttendanceModal(int present, int absent) {
        this.present = present;
        this.absent = absent;
    }

    public AttendanceModal() {
    }

    int present;
    int absent;

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

}
