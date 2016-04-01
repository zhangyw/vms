package com.nanjing.vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.nanjing.vms.R;
import com.nanjing.vms.adapter.EmpLogAdapter;
import com.nanjing.vms.model.EmpLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class EmpLogListActivity extends BaseTitleActivity{

    private ListView lvVehicleLogs;
    private List<EmpLog> mVehicleLogs = new ArrayList<>();
    private EmpLogAdapter mEmpLogAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_logs_list);
        initView();
    }

    private void initView() {
        setTitle("人员出入记录","按条件查询",NO_RES_ID);
        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmpLogListActivity.this,EmpLogSearchListActivity.class);
                startActivity(intent);
            }
        });
        lvVehicleLogs = (ListView) findViewById(R.id.lv_emp_logs);
        mEmpLogAdapter = new EmpLogAdapter(this,mVehicleLogs);
        lvVehicleLogs.setAdapter(mEmpLogAdapter);

    }
}
