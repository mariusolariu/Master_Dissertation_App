package com.sibot.mentorapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Appointment implements Parcelable {
    private String apptKey;
    private String location;
    private String m_code;
    private String date;
    private String start_time;
    private String end_time;

    public Appointment(String apptKey, String location, String m_code, String date, String start_time, String end_time) {
        this.apptKey = apptKey;
        this.location = location;
        this.m_code = m_code;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    protected Appointment(Parcel in) {
        apptKey = in.readString();
        location = in.readString();
        m_code = in.readString();
        date = in.readString();
        start_time = in.readString();
        end_time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(apptKey);
        dest.writeString(location);
        dest.writeString(m_code);
        dest.writeString(date);
        dest.writeString(start_time);
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


    public String getStart_time() {
        return start_time;
    }


    //necessary for firebase. Do not delete
    public Appointment() {

    }

    public String getEnd_time() {
        return end_time;
    }


    public Appointment(String location, String m_code) {
        this.location = location;
        this.m_code = m_code;
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

    public void setApptKey(String apptKey) {
        this.apptKey = apptKey;
    }

    @Override
    public String toString() {
        String buffer = apptKey +
                '\n' +
                m_code +
                "\n" +
                location +
                "\n" +
                start_time +
                "\n" +
                end_time +
                "\n" +
                date;
        return buffer;
    }

    public String getApptKey() {
        return apptKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (!(obj instanceof Appointment)) return false;

        Appointment other = (Appointment) obj;

        return this.m_code.equals(other.m_code) &&
                this.location.equals(other.location) &&
                this.start_time.equals(other.start_time) &&
                this.end_time.equals(other.end_time) &&
                this.date.equals(other.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_code, location, start_time, end_time, date);
    }
}
