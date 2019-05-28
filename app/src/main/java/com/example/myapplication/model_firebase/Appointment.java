package com.example.myapplication.model_firebase;

import android.os.Parcel;
import android.os.Parcelable;

public class Appointment implements Parcelable {
    private String location;
    private String m_code;
    private String date;
    private String start_time;
    private String end_time;

    public Appointment(String location, String m_code, String date, String start_time, String end_time, String feedbackProvided) {
        this.location = location;
        this.m_code = m_code;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.feedbackProvided = feedbackProvided;
    }

    protected Appointment(Parcel in) {
        location = in.readString();
        m_code = in.readString();
        date = in.readString();
        start_time = in.readString();
        feedbackProvided = in.readString();
        end_time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeString(m_code);
        dest.writeString(date);
        dest.writeString(start_time);
        dest.writeString(feedbackProvided);
        dest.writeString(end_time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

    public String getFeedbackProvided() {
        return feedbackProvided;
    }

    public void setFeedbackProvided(String feedbackProvided) {
        this.feedbackProvided = feedbackProvided;
    }

    private String feedbackProvided;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }


    //necessary for firebase. Do not delete
    public Appointment(){

    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEnd_time() {
        return end_time;
    }


    public Appointment(String location, String m_code) {
        this.location = location;
        this.m_code = m_code;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getM_code() {
        return m_code;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setM_code(String m_code) {
        this.m_code = m_code;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(m_code);
        buffer.append("\n");

        buffer.append(location);
        buffer.append("\n");

        buffer.append(start_time);
        buffer.append("\n");

        buffer.append(end_time);
        buffer.append("\n");

        buffer.append(date);

        return buffer.toString();
    }
}
