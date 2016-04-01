package com.nanjing.vms.network;

import android.text.TextUtils;

import com.nanjing.vms.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jacky on 2016/3/7.
 * Version 1.0
 */
public class RequestParameters {


    public static Map<String, String> list_vehicles(String access_token, String vehicle_id, String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("AccessToken", access_token);
        params.put("PageSize", String.valueOf(Constants.PAGE_SIZE_DEFAULT));
        if (!TextUtils.isEmpty(vehicle_id)) {
            params.put("VehicleID", vehicle_id);
        }
        if (!TextUtils.isEmpty(keyword)) {
            params.put("Keyword", keyword);
        }
        return params;
    }


    public static Map<String, String> vehicle_create(String AccessToken, String VehiclePlateLicense, String VehicleAvailableDate,
                                                     String VehicleExpireDate, String VehicleTrafficType, String VehicleDriverName, String VehicleDriverPhone,
                                                     String VehicleDriverOrg, String VehicleOwnerName, String VehicleOwnerPhone, String VehicleOwnerOrg) {
        Map<String, String> params = new HashMap<>();
        params.put("AccessToken", AccessToken);
        params.put("VehiclePlateLicense", VehiclePlateLicense);
        params.put("VehicleAvailableDate", VehicleAvailableDate);
        params.put("VehicleExpireDate", VehicleExpireDate);
        params.put("VehicleTrafficType", VehicleTrafficType);

        if (!TextUtils.isEmpty(VehicleDriverName)) {
            params.put("VehicleDriverName", VehicleDriverName);
        }
        if (!TextUtils.isEmpty(VehicleDriverPhone)) {
            params.put("VehicleDriverPhone", VehicleDriverPhone);
        }
        if (!TextUtils.isEmpty(VehicleDriverOrg)) {
            params.put("VehicleDriverOrg", VehicleDriverOrg);
        }
        if (!TextUtils.isEmpty(VehicleOwnerName)) {
            params.put("VehicleOwnerName", VehicleOwnerName);
        }
        if (!TextUtils.isEmpty(VehicleOwnerPhone)) {
            params.put("VehicleOwnerPhone", VehicleOwnerPhone);
        }
        if (!TextUtils.isEmpty(VehicleOwnerOrg)) {
            params.put("VehicleOwnerOrg", VehicleOwnerOrg);
        }
        return new HashMap<>();
    }


    public static Map<String, String> vehicle_update(String AccessToken, String VehicleID, String VehiclePlateLicense, String VehicleAvailableDate,
                                                     String VehicleExpireDate, String VehicleTrafficType, String VehicleDriverName, String VehicleDriverPhone,
                                                     String VehicleDriverOrg, String VehicleOwnerName, String VehicleOwnerPhone, String VehicleOwnerOrg) {
        Map<String, String> params = new HashMap<>();
        params.put("AccessToken", AccessToken);
        params.put("VehicleID", VehicleID);
        params.put("VehiclePlateLicense", VehiclePlateLicense);
        params.put("VehicleAvailableDate", VehicleAvailableDate);
        params.put("VehicleExpireDate", VehicleExpireDate);
        params.put("VehicleTrafficType", VehicleTrafficType);

        if (!TextUtils.isEmpty(VehicleDriverName)) {
            params.put("VehicleDriverName", VehicleDriverName);
        }
        if (!TextUtils.isEmpty(VehicleDriverPhone)) {
            params.put("VehicleDriverPhone", VehicleDriverPhone);
        }
        if (!TextUtils.isEmpty(VehicleDriverOrg)) {
            params.put("VehicleDriverOrg", VehicleDriverOrg);
        }
        if (!TextUtils.isEmpty(VehicleOwnerName)) {
            params.put("VehicleOwnerName", VehicleOwnerName);
        }
        if (!TextUtils.isEmpty(VehicleOwnerPhone)) {
            params.put("VehicleOwnerPhone", VehicleOwnerPhone);
        }
        if (!TextUtils.isEmpty(VehicleOwnerOrg)) {
            params.put("VehicleOwnerOrg", VehicleOwnerOrg);
        }
        return new HashMap<>();
    }


    public static Map<String, String> list_vehicle_logs(String AccessToken, String StartDate, String EndDate, int page_size, String VehicleLogID, String Keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("AccessToken", AccessToken);
        params.put("StartDate", StartDate);
        params.put("EndDate", EndDate);
        params.put("PageSize", String.valueOf(page_size));
        if (!TextUtils.isEmpty(VehicleLogID)) {
            params.put("VehicleLogID", VehicleLogID);
        }
        if (!TextUtils.isEmpty(Keyword)) {
            params.put("Keyword", Keyword);
        }
        return params;
    }


    public static Map<String, String> list_emps(String AccessToken, int page_size, String EmpID, String Keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("AccessToken", AccessToken);
        params.put("PageSize", String.valueOf(page_size));
        if (!TextUtils.isEmpty(EmpID)) {
            params.put("EmpID", EmpID);
        }
        if (!TextUtils.isEmpty(Keyword)) {
            params.put("Keyword", Keyword);
        }
        return params;
    }


    public static Map<String, String> emp_create(String AccessToken, String EmpName, String EmpGender, String EmpAvailableDate, String EmpExpireDate,
                                                 String EmpCityCardNumber, String EmpCitizenIDNumber, String EmpDeptName, String EmpType,
                                                 String EmpMobile, String EmpCareer) {
        Map<String, String> params = new HashMap<>();
        params.put("AccessToken", AccessToken);
        params.put("EmpName", EmpName);
        params.put("EmpGender", EmpGender);
        params.put("EmpAvailableDate", EmpAvailableDate);
        params.put("EmpExpireDate", EmpExpireDate);
        params.put("EmpCityCardNumber", EmpCityCardNumber);
        params.put("EmpCitizenIDNumber", EmpCitizenIDNumber);
        if (!TextUtils.isEmpty(EmpDeptName)) {
            params.put("EmpDeptName", EmpDeptName);
        }
        if (!TextUtils.isEmpty(EmpType)) {
            params.put("EmpType", EmpType);
        }
        if (!TextUtils.isEmpty(EmpMobile)) {
            params.put("EmpMobile", EmpMobile);
        }
        if (!TextUtils.isEmpty(EmpCareer)) {
            params.put("EmpCareer", EmpCareer);
        }
        return params;
    }


    public static Map<String, String> emp_update(String AccessToken, String EmpID,String EmpName, String EmpGender, String EmpAvailableDate, String EmpExpireDate,
                                                 String EmpCityCardNumber, String EmpCitizenIDNumber, String EmpDeptName, String EmpType,
                                                 String EmpMobile, String EmpCareer) {
        Map<String, String> params = new HashMap<>();
        params.put("AccessToken", AccessToken);
        params.put("EmpID", EmpID);
        params.put("EmpName", EmpName);
        params.put("EmpGender", EmpGender);
        params.put("EmpAvailableDate", EmpAvailableDate);
        params.put("EmpExpireDate", EmpExpireDate);
        params.put("EmpCityCardNumber", EmpCityCardNumber);
        params.put("EmpCitizenIDNumber", EmpCitizenIDNumber);
        if (!TextUtils.isEmpty(EmpDeptName)) {
            params.put("EmpDeptName", EmpDeptName);
        }
        if (!TextUtils.isEmpty(EmpType)) {
            params.put("EmpType", EmpType);
        }
        if (!TextUtils.isEmpty(EmpMobile)) {
            params.put("EmpMobile", EmpMobile);
        }
        if (!TextUtils.isEmpty(EmpCareer)) {
            params.put("EmpCareer", EmpCareer);
        }
        return params;
    }



    public static Map<String, String> list_emp_logs(String AccessToken, String StartDate, String EndDate, int page_size, String EmpLogID, String Keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("AccessToken", AccessToken);
        params.put("StartDate", StartDate);
        params.put("EndDate", EndDate);
        params.put("PageSize", String.valueOf(page_size));
        if (!TextUtils.isEmpty(EmpLogID)) {
            params.put("EmpLogID", EmpLogID);
        }
        if (!TextUtils.isEmpty(Keyword)) {
            params.put("Keyword", Keyword);
        }
        return params;
    }

}
