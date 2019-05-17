package com.example.myapplication.model_firebase;

public class Appointment {
    public String appointment_name;
    private String location;
    private String m_code;
    private String date;
    private String start_time;

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

    private String end_time;

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
}
