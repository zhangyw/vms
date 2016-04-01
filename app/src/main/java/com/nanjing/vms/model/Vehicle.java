package com.nanjing.vms.model;

import com.nanjing.vms.Constants;

import java.io.Serializable;

/**
 * Created by Jacky on 2016/2/21.
 * Version 1.0
 */
public class Vehicle implements Serializable {

    private String VehicleID;
    private String VehiclePlateLicense;
    private String VehicleAvailableDate;
    private String VehicleExpireDate;
    private int VehicleTrafficType;
    private String VehicleDriverName;
    private String VehicleDriverPhone;
    private String VehicleDriverOrg;
    private String VehicleOwnerName;
    private String VehicleOwnerPhone;
    private String VehicleOwnerOrg;
    private int VehicleStatus;

    public String getVehicleID() {
        return VehicleID;
    }

    public void setVehicleID(String vehicleID) {
        VehicleID = vehicleID;
    }

    public String getVehiclePlateLicense() {
        return VehiclePlateLicense;
    }

    public void setVehiclePlateLicense(String vehiclePlateLicense) {
        VehiclePlateLicense = vehiclePlateLicense;
    }

    public String getVehicleAvailableDate() {
        return VehicleAvailableDate;
    }

    public void setVehicleAvailableDate(String vehicleAvailableDate) {
        VehicleAvailableDate = vehicleAvailableDate;
    }

    public String getVehicleExpireDate() {
        return VehicleExpireDate;
    }

    public void setVehicleExpireDate(String vehicleExpireDate) {
        VehicleExpireDate = vehicleExpireDate;
    }

    public int getVehicleTrafficType() {
        return VehicleTrafficType;
    }

    public void setVehicleTrafficType(int vehicleTrafficType) {
        VehicleTrafficType = vehicleTrafficType;
    }

    public String getVehicleDriverName() {
        return VehicleDriverName;
    }

    public void setVehicleDriverName(String vehicleDriverName) {
        VehicleDriverName = vehicleDriverName;
    }

    public String getVehicleDriverPhone() {
        return VehicleDriverPhone;
    }

    public void setVehicleDriverPhone(String vehicleDriverPhone) {
        VehicleDriverPhone = vehicleDriverPhone;
    }

    public String getVehicleDriverOrg() {
        return VehicleDriverOrg;
    }

    public void setVehicleDriverOrg(String vehicleDriverOrg) {
        VehicleDriverOrg = vehicleDriverOrg;
    }

    public String getVehicleOwnerName() {
        return VehicleOwnerName;
    }

    public void setVehicleOwnerName(String vehicleOwnerName) {
        VehicleOwnerName = vehicleOwnerName;
    }

    public String getVehicleOwnerPhone() {
        return VehicleOwnerPhone;
    }

    public void setVehicleOwnerPhone(String vehicleOwnerPhone) {
        VehicleOwnerPhone = vehicleOwnerPhone;
    }

    public String getVehicleOwnerOrg() {
        return VehicleOwnerOrg;
    }

    public void setVehicleOwnerOrg(String vehicleOwnerOrg) {
        VehicleOwnerOrg = vehicleOwnerOrg;
    }

    public int getVehicleStatus() {
        return VehicleStatus;
    }

    public void setVehicleStatus(int vehicleStatus) {
        VehicleStatus = vehicleStatus;
    }


    public String getVehicleStatusStr() {
        int status = getVehicleStatus();
        String str = "";
        switch (status) {
            case Constants.VehicleStatus.NORMAL:
                str = "正常";
                break;
            case Constants.VehicleStatus.PENDING:
                str = "待审批";
                break;
            case Constants.VehicleStatus.DELETE:
                str = "删除";
                break;
            case Constants.VehicleStatus.FORBID:
                str = "禁止";
                break;
            default:
                str = "禁止";
                break;
        }
        return str;
    }


}
