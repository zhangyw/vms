package com.nanjing.vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nanjing.vms.Constants;
import com.nanjing.vms.R;
import com.nanjing.vms.cache.Share;
import com.nanjing.vms.model.Member;
import com.nanjing.vms.model.Vehicle;
import com.nanjing.vms.network.BaseRespCallback;
import com.nanjing.vms.network.RetrofitClient;
import com.nanjing.vms.network.resp.BaseResp;
import com.nanjing.vms.utils.DateUtils;
import com.nanjing.vms.utils.ToastUtils;
import com.nanjing.vms.utils.UiUtils;

import retrofit2.Call;

/**
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class VehicleDetailsActivity extends BaseTitleActivity implements View.OnClickListener {

    private Vehicle mVehicle;

    private TextView tvPlateLicense;
    private TextView tvAvailableDate;
    private TextView tvExpireDate;
    private TextView tvTrafficType;
    private TextView tvDriverName;
    private TextView tvDriverPhone;
    private TextView tvDriverOrg;
    private TextView tvOwnerName;
    private TextView tvOwnerPhone;
    private TextView tvOwnerOrg;
    private TextView tvStatus;

    private Button btnDelete;
    private Button btnBlack;
    private Button btnApprove, btnDisapprove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
        mVehicle = (Vehicle) getIntent().getExtras().getSerializable("vehicle");
        initView();
        setVehicleData(mVehicle);
    }

    private void initView() {
        setTitle("车辆详情", "编辑", NO_RES_ID);
        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
                if (Member.hasPermission(member.getmMemberType(), Constants.MemberPermission.VEHICLE_UPDATE)) {
                    Intent intent = new Intent(VehicleDetailsActivity.this, VehicleEditActivity.class);
                    intent.putExtra("vehicle",mVehicle);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(getApplicationContext(), getString(R.string.no_permission_tip));
                }
            }
        });
        tvPlateLicense = (TextView) findViewById(R.id.tv_plate_license);
        tvAvailableDate = (TextView) findViewById(R.id.tv_available_date);
        tvExpireDate = (TextView) findViewById(R.id.tv_expire_date);
        tvTrafficType = (TextView) findViewById(R.id.tv_traffic_type);
        tvDriverName = (TextView) findViewById(R.id.tv_driver_name);
        tvDriverPhone = (TextView) findViewById(R.id.tv_driver_phone);
        tvDriverOrg = (TextView) findViewById(R.id.tv_driver_org);
        tvOwnerName = (TextView) findViewById(R.id.tv_owner_name);
        tvOwnerPhone = (TextView) findViewById(R.id.tv_owner_phone);
        tvOwnerOrg = (TextView) findViewById(R.id.tv_owner_org);
        tvStatus = (TextView) findViewById(R.id.tv_status);

        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
        btnBlack = (Button) findViewById(R.id.btn_black);
        btnBlack.setOnClickListener(this);
        btnApprove = (Button) findViewById(R.id.btn_approve);
        btnApprove.setOnClickListener(this);
        btnDisapprove = (Button) findViewById(R.id.btn_disapprove);
        btnDisapprove.setOnClickListener(this);
    }

    private void setVehicleData(Vehicle vehicle) {
        tvPlateLicense.setText(vehicle.getVehiclePlateLicense());
        tvAvailableDate.setText(DateUtils.formatTime(vehicle.getVehicleAvailableDate()));
        tvExpireDate.setText(DateUtils.formatTime(vehicle.getVehicleExpireDate()));
        tvTrafficType.setText(UiUtils.getVehicleTrafficType(vehicle.getVehicleTrafficType()));
        tvDriverName.setText(vehicle.getVehicleDriverName());
        tvDriverPhone.setText(vehicle.getVehicleDriverPhone());
        tvDriverOrg.setText(vehicle.getVehicleDriverOrg());
        tvOwnerName.setText(vehicle.getVehicleOwnerName());
        tvOwnerPhone.setText(vehicle.getVehicleOwnerPhone());
        tvOwnerOrg.setText(vehicle.getVehicleDriverOrg());
        tvStatus.setText(vehicle.getVehicleStatusStr());
    }

    @Override
    public void onClick(View v) {
        Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
        switch (v.getId()) {
            case R.id.btn_delete:
                if (Member.hasPermission(member.getmMemberType(), Constants.MemberPermission.VEHICLE_DELETE)) {
                    deleteVehicle(mVehicle.getVehicleID());
                } else {
                    ToastUtils.showToast(getApplicationContext(), getString(R.string.no_permission_tip));
                }
                break;
            case R.id.btn_black:
                if (Member.hasPermission(member.getmMemberType(), Constants.MemberPermission.VEHICLE_BLACK)) {
                    blackVehicle(mVehicle.getVehicleID());
                } else {
                    ToastUtils.showToast(getApplicationContext(), getString(R.string.no_permission_tip));
                }
                break;
            case R.id.btn_approve:
                if (Member.hasPermission(member.getmMemberType(), Constants.MemberPermission.VEHICLE_BLACK)) {
                    checkVehicle(mVehicle.getVehicleID(), Constants.VEHICLE_CHECK_THROUGH);
                } else {
                    ToastUtils.showToast(getApplicationContext(), getString(R.string.no_permission_tip));
                }
                break;
            case R.id.btn_disapprove:
                if (Member.hasPermission(member.getmMemberType(), Constants.MemberPermission.VEHICLE_BLACK)) {
                    checkVehicle(mVehicle.getVehicleID(), Constants.VEHICLE_CHECK_NOT_THROUGH);
                } else {
                    ToastUtils.showToast(getApplicationContext(), getString(R.string.no_permission_tip));
                }
                break;
        }
    }


    private void deleteVehicle(String vehicle_id) {
        showLoadingDialog();
        Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
        Call<BaseResp> call = RetrofitClient.vmsApis().vehicle_delete(member.getAccessToken(), vehicle_id);
        call.enqueue(new BaseRespCallback(getApplicationContext()) {
            @Override
            public void onSuccess(BaseResp baseResp) {
                ToastUtils.showToast(getApplicationContext(), "删除成功");
                VehicleDetailsActivity.this.finish();
            }

            @Override
            public void onAfter() {
                dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }


    private void blackVehicle(String vehicle_id) {
        showLoadingDialog();
        Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
        Call<BaseResp> call = RetrofitClient.vmsApis().vehicle_black(member.getAccessToken(), vehicle_id);
        call.enqueue(new BaseRespCallback(getApplicationContext()) {
            @Override
            public void onSuccess(BaseResp baseResp) {
                ToastUtils.showToast(getApplicationContext(), "加入黑名单成功");
                VehicleDetailsActivity.this.finish();
            }

            @Override
            public void onAfter() {
                dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }


    private void checkVehicle(String vehicle_id, int check_status) {
        showLoadingDialog();
        Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
        Call<BaseResp> call = RetrofitClient.vmsApis().vehicle_check(member.getAccessToken(), vehicle_id, check_status);
        call.enqueue(new BaseRespCallback(getApplicationContext()) {
            @Override
            public void onSuccess(BaseResp baseResp) {
                ToastUtils.showToast(getApplicationContext(), "已审批");
                VehicleDetailsActivity.this.finish();
            }

            @Override
            public void onAfter() {
                dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }
}
