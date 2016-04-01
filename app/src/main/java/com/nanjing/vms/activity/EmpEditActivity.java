package com.nanjing.vms.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nanjing.vms.R;
import com.nanjing.vms.model.Emp;
import com.nanjing.vms.utils.DateUtils;
import com.nanjing.vms.utils.UiUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class EmpEditActivity extends BaseTitleActivity {

    private Emp mEmp;

    private TextView tvAvailableDate;
    private TextView tvExpireDate;
    private TextView tvEmpType;
    private TextView tvGender;
    private EditText etName, etCityCardNumber, etCitizenId, etCareer, etDeptName, etMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_edit);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("emp")) {
            mEmp = (Emp) bundle.getSerializable("emp");
        }
        initView();
        if (mEmp != null) {
            setEmpData(mEmp);
        } else {
            tvAvailableDate.setText(DateUtils.SDF_TIME_DISPLAY.format(new Date()));
            tvExpireDate.setText("请选择");
        }
    }


    private void initView() {
        if (mEmp != null) {
            setTitle("新增人员", "保存", NO_RES_ID);
        } else {
            setTitle("编辑人员", "保存", NO_RES_ID);
        }

        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmp == null) {//新增

                } else {//编辑

                }
            }
        });


        tvAvailableDate = (TextView) findViewById(R.id.tv_available_date);
        tvExpireDate = (TextView) findViewById(R.id.tv_expire_date);
        tvEmpType = (TextView) findViewById(R.id.tv_emp_type);
        tvGender = (TextView) findViewById(R.id.tv_gender);
        etName = (EditText) findViewById(R.id.et_name);
        etCitizenId = (EditText) findViewById(R.id.et_citizen_id);
        etCityCardNumber = (EditText) findViewById(R.id.et_city_card_number);
        etCareer = (EditText) findViewById(R.id.et_career);
        etDeptName = (EditText) findViewById(R.id.et_dept_name);
        etMobile = (EditText) findViewById(R.id.et_mobile);


        tvAvailableDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(tvAvailableDate);
            }
        });


        tvEmpType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectEmpTypeDialog();
            }
        });


    }


    //获取时间
    public void getDate(final TextView tv) {
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
                final TimePickerDialog timePickerDialog = new TimePickerDialog(EmpEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sb.delete(0, sb.length());
                        sb.append(String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                        sb.append("\u0020");
                        sb.append(String.format("%02d:%02d", hourOfDay, minute));
                        tv.setText(sb.toString());
                    }
                }, hourOfDay, minute, true);

                timePickerDialog.show();
            }
        });
        datePickerDialog.show();
    }


    private void showSelectEmpTypeDialog() {
        final String[] typesString = getResources().getStringArray(R.array.EmpTypeStr);
        final int[] typesInt = getResources().getIntArray(R.array.TrafficTypeInt);
        new AlertDialog.Builder(EmpEditActivity.this).setTitle("请选择")
                .setItems(typesString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        tvEmpType.setText(typesString[which]);
                        tvEmpType.setTag(typesInt[which]);
                    }
                }).show();
    }

    private void setEmpData(Emp emp){
        etName.setText(emp.getEmpName());
        tvAvailableDate.setText(DateUtils.formatTime(emp.getEmpAvailableDate()));
        tvExpireDate.setText(DateUtils.formatTime(emp.getEmpExpireDate()));
        tvGender.setText(emp.getEmpGender());
        tvEmpType.setText(UiUtils.getEmpType(Integer.parseInt(emp.getEmpType())));
        etDeptName.setText(emp.getEmpDeptName());
        etCareer.setText(emp.getEmpCareer());
        etCitizenId.setText(emp.getEmpCitizenIDNumber());
        etCityCardNumber.setText(emp.getEmpCityCardNumber());
        etMobile.setText(emp.getEmpMobile());
    }
}
