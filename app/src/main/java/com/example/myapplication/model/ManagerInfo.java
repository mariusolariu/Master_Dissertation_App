package com.example.myapplication.model;

public class ManagerInfo {
    public String m_phone_no;
    public String manager_name;

    public ManagerInfo() {
        //firebase
    }

    public ManagerInfo(String m_phone_no, String manager_name) {
        this.m_phone_no = m_phone_no;
        this.manager_name = manager_name;
    }
}
