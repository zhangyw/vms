package com.nanjing.vms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanjing.vms.R;
import com.nanjing.vms.model.EmpLog;
import com.nanjing.vms.utils.DateUtils;
import com.nanjing.vms.utils.UiUtils;

import java.util.List;

/**
 * Created by Jacky on 2016/2/22.
 * Version 1.0
 */
public class EmpLogAdapter extends CustomBaseAdapter<EmpLog> {

    public EmpLogAdapter(Context context, List<EmpLog> list) {
        super(context,list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder;
        if (convertView == null) {
            holder  = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_emp_logs, null);
            holder.tv_emp_name = (TextView) convertView.findViewById(R.id.tv_emp_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_gateway_name = (TextView) convertView.findViewById(R.id.tv_gateway_name);
            holder.tv_traffic_type = (TextView) convertView.findViewById(R.id.tv_traffic_type);
            holder.tv_traffic_order = (TextView) convertView.findViewById(R.id.tv_order);
            holder.tv_card_number = (TextView) convertView.findViewById(R.id.tv_card_number);
            holder.tv_dept_name = (TextView) convertView.findViewById(R.id.tv_dept_name);
            holder.tv_emp_type = (TextView) convertView.findViewById(R.id.tv_emp_type);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        EmpLog vehicleLog = getList().get(position);
        holder.tv_emp_name.setText("人员姓名："+vehicleLog.getEmpLogEmpName());
        holder.tv_time.setText("通行时间："+DateUtils.formatTime(vehicleLog.getEmpLogCreationDate()));
        holder.tv_gateway_name.setText("通行地点："+vehicleLog.getEmpLogGatewayName());
        holder.tv_traffic_type.setText("通行类型：" + UiUtils.getVehicleTrafficType(Integer.parseInt(vehicleLog.getEmpLogTrafficType())));
        holder.tv_traffic_order.setText(vehicleLog.getEmpLogTrafficOrder());
        holder.tv_emp_name.setText("所属单位："+vehicleLog.getEmpDeptName());
        holder.tv_card_number.setText("通行卡号："+vehicleLog.getEmpLogEmpName());
        holder.tv_emp_type.setText("人员类型："+vehicleLog.getEmpType());
        return convertView;
    }


    class ViewHolder {
        private TextView tv_emp_name;
        private TextView tv_time;
        private TextView tv_traffic_type;
        private TextView tv_gateway_name;
        private TextView tv_card_number;
        private TextView tv_dept_name;
        private TextView tv_emp_type;
        private TextView tv_traffic_order;

    }
}
