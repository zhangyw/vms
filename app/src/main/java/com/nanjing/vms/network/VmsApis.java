package com.nanjing.vms.network;

import com.nanjing.vms.network.resp.BankAdvertisements;
import com.nanjing.vms.network.resp.BaseResp;
import com.nanjing.vms.network.resp.ListEmpLogsResp;
import com.nanjing.vms.network.resp.ListEmpsResp;
import com.nanjing.vms.network.resp.ListVehiclesResp;
import com.nanjing.vms.network.resp.LoginResp;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Jacky on 2016/3/6.
 * Version 1.0
 */
public interface VmsApis {


    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("member/loginprocess")
    Call<LoginResp> loginprocess(@Field("MemberName") String memberName, @Field("MemberPassword") String password);

    @GET("vehicle/listvehicles")
    Call<ListVehiclesResp> list_vehicles(@QueryMap Map<String, String> options);

    @POST("vehicle/create")
    Call<BaseResp> vehicle_create(@FieldMap Map<String, String> params);

    @POST("vehicle/update")
    Call<BaseResp> vehicle_update(@FieldMap Map<String, String> params);

    @POST("vehicle/delete")
    Call<BaseResp> vehicle_delete(@Field("AccessToken") String access_token, @Field("VehicleID") String vehicle_id);

    /**
     * 车辆加入黑名单
     */
    @POST("vehicle/black")
    Call<BaseResp> vehicle_black(@Field("AccessToken") String access_token, @Field("VehicleID") String vehicle_id);

    @POST("vehicle/check")
    Call<BaseResp> vehicle_check(@Field("AccessToken") String access_token, @Field("VehicleID") String vehicle_id, @Field("CheckStatus") int status);

    @GET("vehicle/listpendingvehicles")
    Call<ListVehiclesResp> list_pending_vehicles(@Query("AccessToken") String access_token);

    //TODO 缺少接口
    @GET("vehicle/listpendingvehicles")
    Call<ListVehiclesResp> list_black_vehicles(@Query("AccessToken") String access_token);

    /**
     * 车辆出入记录列表
     */
    @GET("vehicle/listvehiclelogs")
    Call<ListVehiclesResp> list_vehicle_logs(@QueryMap Map<String, String> options);

    @GET("emp/listemps")
    Call<ListEmpsResp> list_emps(@QueryMap Map<String, String> options);

    /**新增人员*/
    @POST("emp/create")
    Call<BaseResp> emp_create(@FieldMap Map<String, String> params);

    @POST("emp/update")
    Call<BaseResp> emp_update(@FieldMap Map<String, String> params);

    @POST("emp/delete")
    Call<BaseResp> emp_delete(@Field("AccessToken") String AccessToken,@Field("EmpID") String EmpId);

    @POST("emp/check")
    Call<BaseResp> emp_check(@Field("AccessToken") String AccessToken,@Field("EmpID") String EmpId);

    @GET("emp/listpendingemps")
    Call<ListVehiclesResp> list_pending_emps(@Query("AccessToken") String access_token);

    /** 查询人员出入记录列表*/
    @GET("emp/listemplogs")
    Call<ListEmpLogsResp> list_emp_logs(@QueryMap Map<String, String> options);


    @GET("bank_advertisements")
    Call<BankAdvertisements> bank_advertisements(@QueryMap Map<String, String> options);

    @GET("bank_advertisements")
    Observable<BankAdvertisements> bank_advertisement(@QueryMap Map<String, String> options);
}
