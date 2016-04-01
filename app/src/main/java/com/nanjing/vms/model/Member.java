package com.nanjing.vms.model;

import com.google.gson.annotations.SerializedName;
import com.nanjing.vms.Constants;

import java.io.Serializable;

/**
 * Created by Jacky on 2016/2/21.
 * Version 1.0
 */
public class Member implements Serializable, Constants.MemberPermission, Constants.MemberType {
    private String MemberID;
    private String AccessToken;
    @SerializedName("MemberType")
    private int mMemberType;
    private String monitorUsername;
    private String monitorPassword;

    public String getMonitorPassword() {
        return monitorPassword;
    }

    public void setMonitorPassword(String monitorPassword) {
        this.monitorPassword = monitorPassword;
    }

    public String getMonitorUsername() {
        return monitorUsername;
    }

    public void setMonitorUsername(String monitorUsername) {
        this.monitorUsername = monitorUsername;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String memberID) {
        MemberID = memberID;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public int getmMemberType() {
        return mMemberType;
    }

    public void setmMemberType(int mMemberType) {
        this.mMemberType = mMemberType;
    }


    /**
     * 判断用户是否拥有某种权限
     * @param memberType
     * @param memberPermission
     * @return
     */
    public static boolean hasPermission(int memberType, int memberPermission) {
        if (memberType == Constants.MemberType.TYPE_SUPER_ADMIN)//超级管理员
            return true;
        else if (memberType == TYPE_FRONT_OPERATOR)//前台操作员
            return isContains(memberPermission,FRONT_OPERATOR_PERMISSIONS);
        else if(memberType == TYPE_BACK_OPERATOR)//后台登记操作员
            return isContains(memberPermission,BACK_OPERATOR_PERMISSIONS);
        else if(memberType == TYPE_CHECK_ADMIN)//数据审批管理员
            return isContains(memberPermission, CHECK_ADMIN_PERMISSIONS);
        else if(memberType == TYPE_DATA_AMDIN)//数据管理员
            return isContains(memberPermission,DATA_ADMIN_PERMISSIONS);
        else if(memberType == TYPE_BACK_SHIFT_OPERATOR)//后台值班操作员
            return isContains(memberPermission,BACK_SHIFT_OPERATOR_PERMISSIONS);
        return false;
    }

    private static boolean isContains(int memberPermission, int[] permissions) {
        for (int per : permissions) {
            if (per == memberPermission) {
                return true;
            }
        }
        return false;
    }


}
