package com.minor.attendancemanager2.home;

import android.os.Parcel;
import android.os.Parcelable;

public class SubjectModel implements Parcelable {

    public SubjectModel(String subject, String lecture, String subjectId) {
        this.subject = subject;
        this.lecture = lecture;
        this.subjectId = subjectId;
    }

    String subject;
    String lecture;
    String subjectId;

    public static final Creator<SubjectModel> CREATOR = new Creator<SubjectModel>() {
        @Override
        public SubjectModel createFromParcel(Parcel in) {
            return new SubjectModel(in);
        }

        @Override
        public SubjectModel[] newArray(int size) {
            return new SubjectModel[size];
        }
    };

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    protected SubjectModel(Parcel in){
        subject = in.readString();
        lecture = in.readString();
        subjectId = in.readString();
    }

    public void writeToParcel(Parcel dest,int flags){
        dest.writeString(subject);
        dest.writeString(lecture);
        dest.writeString(subjectId);
    }

    public int describeContents(){
        return 0;
    }


    public SubjectModel() {;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

}
