package com.nanjing.vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nanjing.vms.R;
import com.nanjing.vms.model.Emp;
import com.nanjing.vms.utils.DateUtils;
import com.nanjing.vms.utils.UiUtils;

/**
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class EmpDetailsActivity extends BaseTitleActivity implements View.OnClickListener{

    private Emp mEmp;

    private TextView tvName,tvGender;
    private TextView tvAvailableDate;
    private TextView tvExpireDate;
    private TextView tvCityCardNumber;
    private TextView tvCitizenId;
    private TextView tvDeptName;
    private TextView tvEmpType;
    private TextView tvMobile;
    private TextView tvCareer;

    private Button btnDelete;
    private Button btnApprove, btnDisapprove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_details);
        mEmp = (Emp) getIntent().getExtras().getSerializable("emp");
        initView();
        setVehicleData(mEmp);
    }

    private void initView() {
        setTitle("人员详情", "编辑", NO_RES_ID);
        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmpDetailsActivity.this,EmpEditActivity.class);
                intent.putExtra("emp",mEmp);
                startActivity(intent);
            }
        });
        tvName = (TextView) findViewById(R.id.tv_name);
        tvAvailableDate = (TextView) findViewById(R.id.tv_available_date);
        tvExpireDate = (TextView) findViewById(R.id.tv_expire_date);
        tvCityCardNumber = (TextView) findViewById(R.id.tv_traffic_type);
        tvCitizenId = (TextView) findViewById(R.id.tv_driver_name);
        tvMobile = (TextView) findViewById(R.id.tv_driver_phone);
        tvCareer = (TextView) findViewById(R.id.tv_driver_org);
        tvDeptName = (TextView) findViewById(R.id.tv_owner_name);
        tvEmpType = (TextView) findViewById(R.id.tv_owner_phone);
        tvGender = (TextView) findViewById(R.id.tv_owner_org);

        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
        btnApprove = (Button) findViewById(R.id.btn_approve);
        btnApprove.setOnClickListener(this);
        btnDisapprove = (Button) findViewById(R.id.btn_disapprove);
        btnDisapprove.setOnClickListener(this);
    }

    private void setVehicleData(Emp emp){
        tvName.setText(emp.getEmpName());
        tvAvailableDate.setText(DateUtils.formatTime(emp.getEmpAvailableDate()));
        tvExpireDate.setText(DateUtils.formatTime(emp.getEmpExpireDate()));
        tvGender.setText(emp.getEmpGender());
        tvEmpType.setText(UiUtils.getEmpType(Integer.parseInt(emp.getEmpType())));
        tvDeptName.setText(emp.getEmpDeptName());
        tvCareer.setText(emp.getEmpCareer());
        tvCitizenId.setText(emp.getEmpCitizenIDNumber());
        tvCityCardNumber.setText(emp.getEmpCityCardNumber());
        tvMobile.setText(emp.getEmpMobile());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                break;
            case R.id.btn_black:
                break;
            case R.id.btn_approve:
                break;
            case R.id.btn_disapprove:
                break;
        }

    }
}
