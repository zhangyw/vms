package com.nanjing.vms.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nanjing.vms.R;
import com.nanjing.vms.cache.Share;
import com.nanjing.vms.model.Member;
import com.nanjing.vms.model.Vehicle;
import com.nanjing.vms.network.BaseRespCallback;
import com.nanjing.vms.network.RequestParameters;
import com.nanjing.vms.network.RetrofitClient;
import com.nanjing.vms.network.resp.BaseResp;
import com.nanjing.vms.utils.DateUtils;
import com.nanjing.vms.utils.ToastUtils;
import com.nanjing.vms.utils.UiUtils;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;

/**
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class VehicleEditActivity extends BaseTitleActivity {
    private Vehicle mVehicle;

    private EditText tvPlateLicense;
    private TextView tvAvailableDate;
    private TextView tvExpireDate;
    private TextView tvTrafficType;
    private EditText tvDriverName;
    private EditText tvDriverPhone;
    private EditText tvDriverOrg;
    private EditText tvOwnerName;
    private EditText tvOwnerPhone;
    private EditText tvOwnerOrg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_edit);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("vehicle")) {
            mVehicle = (Vehicle) bundle.getSerializable("vehicle");
        }
        initView();
        if (mVehicle != null) {
            setVehicleData(mVehicle);
        } else {
            tvAvailableDate.setText(DateUtils.SDF_TIME_DISPLAY.format(new Date()));
            tvExpireDate.setText("请选择");
        }
    }

    private void initView() {
        if (mVehicle != null) {
            setTitle("车辆编辑", "保存", NO_RES_ID);
        } else {
            setTitle("车辆新增", "保存", NO_RES_ID);
        }

        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVehicle == null) {//新增
                    createVehicle();
                } else {//编辑
                    updateVehicle();
                }
            }
        });

        tvPlateLicense = (EditText) findViewById(R.id.tv_plate_license);
        tvAvailableDate = (TextView) findViewById(R.id.tv_available_date);
        tvAvailableDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDateDialog(tvAvailableDate);
            }
        });
        tvExpireDate = (TextView) findViewById(R.id.tv_expire_date);
        tvExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDateDialog(tvExpireDate);
            }
        });
        tvTrafficType = (TextView) findViewById(R.id.tv_traffic_type);
        tvTrafficType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectTrafficTypeDialog();
            }
        });
        tvDriverName = (EditText) findViewById(R.id.tv_driver_name);
        tvDriverPhone = (EditText) findViewById(R.id.tv_driver_phone);
        tvDriverOrg = (EditText) findViewById(R.id.tv_driver_org);
        tvOwnerName = (EditText) findViewById(R.id.tv_owner_name);
        tvOwnerPhone = (EditText) findViewById(R.id.tv_owner_phone);
        tvOwnerOrg = (EditText) findViewById(R.id.tv_owner_org);


        tvTrafficType.setText(getResources().getStringArray(R.array.TrafficTypeStr)[0]);
        tvTrafficType.setTag(getResources().getIntArray(R.array.TrafficTypeInt)[0]);

    }


    //选择时间
    public void showSelectDateDialog(final TextView tv) {
        final StringBuffer sb = new StringBuffer();
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int minute = calendar.get(Calendar.MINUTE);
        final int monthOfYear = calendar.get(Calendar.MONTH);
        final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        }, year, monthOfYear, dayOfMonth);
        final DatePicker datePicker = datePickerDialog.getDatePicker();
        datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final TimePickerDialog timePickerDialog = new TimePickerDialog(VehicleEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sb.delete(0, sb.length());
                        sb.append(String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                        sb.append("\u0020");
                        sb.append(String.format("%02d:%02d", hourOfDay, minute));
                        tv.setText(sb.toString());
                        tv.setTag(sb.toString());
                    }
                }, hourOfDay, minute, true);

                timePickerDialog.show();
            }
        });
        datePickerDialog.show();
    }


    private void showSelectTrafficTypeDialog() {
        final String[] typesString = getResources().getStringArray(R.array.TrafficTypeStr);
        final int[] typesInt = getResources().getIntArray(R.array.TrafficTypeInt);
        new AlertDialog.Builder(VehicleEditActivity.this).setTitle("请选择")
                .setItems(typesString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        tvTrafficType.setText(typesString[which]);
                        tvTrafficType.setTag(typesInt[which]);
                    }
                }).show();
    }

    private void setVehicleData(Vehicle vehicle) {
        tvPlateLicense.setText(vehicle.getVehiclePlateLicense());
        tvAvailableDate.setText(DateUtils.formatTime(vehicle.getVehicleAvailableDate()));
        tvExpireDate.setText(DateUtils.formatTime(vehicle.getVehicleExpireDate()));
        tvTrafficType.setText(UiUtils.getVehicleTrafficType(vehicle.getVehicleTrafficType()));
        tvTrafficType.setTag(vehicle.getVehicleTrafficType());
        tvDriverName.setText(vehicle.getVehicleDriverName());
        tvDriverPhone.setText(vehicle.getVehicleDriverPhone());
        tvDriverOrg.setText(vehicle.getVehicleDriverOrg());
        tvOwnerName.setText(vehicle.getVehicleOwnerName());
        tvOwnerPhone.setText(vehicle.getVehicleOwnerPhone());
        tvOwnerOrg.setText(vehicle.getVehicleDriverOrg());
    }


    private void updateVehicle() {
        Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
        if (checkInput()) {
            Call<BaseResp> callUpdate = RetrofitClient.vmsApis().vehicle_update(RequestParameters.vehicle_update(member.getAccessToken(),
                    mVehicle.getVehicleID(), getTextNoSpace(tvPlateLicense), getTextNoSpace(tvAvailableDate),
                    getTextNoSpace(tvExpireDate), (String) (tvTrafficType.getTag()), getTextNoSpace(tvDriverName),
                    getTextNoSpace(tvDriverPhone), getTextNoSpace(tvDriverOrg), getTextNoSpace(tvOwnerName),
                    getTextNoSpace(tvOwnerPhone), getTextNoSpace(tvOwnerOrg)));
            showLoadingDialog();
            callUpdate.enqueue(new BaseRespCallback<BaseResp>(getApplicationContext()) {
                @Override
                public void onSuccess(BaseResp baseResp) {
                    ToastUtils.showToast(getApplicationContext(), "修改成功");
                    VehicleEditActivity.this.finish();
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

    private void createVehicle() {
        Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
        if (checkInput()) {
            Call<BaseResp> callUpdate = RetrofitClient.vmsApis().vehicle_create(RequestParameters.vehicle_create(member.getAccessToken(),
                    getTextNoSpace(tvPlateLicense), getTextNoSpace(tvAvailableDate),
                    getTextNoSpace(tvExpireDate), (String) (tvTrafficType.getTag()), getTextNoSpace(tvDriverName),
                    getTextNoSpace(tvDriverPhone), getTextNoSpace(tvDriverOrg), getTextNoSpace(tvOwnerName),
                    getTextNoSpace(tvOwnerPhone), getTextNoSpace(tvOwnerOrg)));
            showLoadingDialog();
            callUpdate.enqueue(new BaseRespCallback<BaseResp>(getApplicationContext()) {
                @Override
                public void onSuccess(BaseResp baseResp) {
                    ToastUtils.showToast(getApplicationContext(), "修改成功");
                    VehicleEditActivity.this.finish();
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

    private boolean checkInput() {
        if (TextUtils.isEmpty(getTextNoSpace(tvPlateLicense))) {
            ToastUtils.showToast(getApplicationContext(), "请输入车牌号");
            return false;
        }

        if(TextUtils.isEmpty(getTextNoSpace(tvExpireDate))){
            ToastUtils.showToast(getApplicationContext(),"请选择截至日期");
            return false;
        }
        return true;
    }
}
