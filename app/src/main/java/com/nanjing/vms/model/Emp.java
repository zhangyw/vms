package com.nanjing.vms.model;

import java.io.Serializable;

/**
 * Created by Jacky on 2016/2/22.
 * Version 1.0
 */
public class Emp implements Serializable{

    private String EmpID;
    private String EmpName;
    private String EmpGender;
    private String EmpAvailableDate;
    private String EmpExpireDate;
    private String EmpCityCardNumber;
    private String EmpCitizenIDNumber;
    private String EmpDeptName;
    private String EmpType;
    private String EmpMobile;
    private String EmpCareer;

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getEmpGender() {
        return EmpGender;
    }

    public void setEmpGender(String empGender) {
        EmpGender = empGender;
    }

    public String getEmpAvailableDate() {
        return EmpAvailableDate;
    }

    public void setEmpAvailableDate(String empAvailableDate) {
        EmpAvailableDate = empAvailableDate;
    }

    public String getEmpExpireDate() {
        return EmpExpireDate;
    }

    public void setEmpExpireDate(String empExpireDate) {
        EmpExpireDate = empExpireDate;
    }

    public String getEmpCityCardNumber() {
        return EmpCityCardNumber;
    }

    public void setEmpCityCardNumber(String empCityCardNumber) {
        EmpCityCardNumber = empCityCardNumber;
    }

    public String getEmpCitizenIDNumber() {
        return EmpCitizenIDNumber;
    }

    public void setEmpCitizenIDNumber(String empCitizenIDNumber) {
        EmpCitizenIDNumber = empCitizenIDNumber;
    }

    public String getEmpDeptName() {
        return EmpDeptName;
    }

    public void setEmpDeptName(String empDeptName) {
        EmpDeptName = empDeptName;
    }

    public String getEmpType() {
        return EmpType;
    }

    public void setEmpType(String empType) {
        EmpType = empType;
    }

    public String getEmpMobile() {
        return EmpMobile;
    }

    public void setEmpMobile(String empMobile) {
        EmpMobile = empMobile;
    }

    public String getEmpCareer() {
        return EmpCareer;
    }

    public void setEmpCareer(String empCareer) {
        EmpCareer = empCareer;
    }
}
