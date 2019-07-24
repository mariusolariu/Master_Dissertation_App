package com.sibot.mentorapp.model;

public class ManagerInfo {
    public String m_phone_no;
    public String manager_name;

    public String getM_phone_no() {
        return m_phone_no;
    }

    public void setM_phone_no(String m_phone_no) {
        this.m_phone_no = m_phone_no;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public ManagerInfo() {
        //firebase
    }

    public ManagerInfo(String m_phone_no, String manager_name) {
        this.m_phone_no = m_phone_no;
        this.manager_name = manager_name;
    }
}
