package com.nanjing.vms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanjing.vms.R;
import com.nanjing.vms.model.Vehicle;
import com.nanjing.vms.utils.DateUtils;
import com.nanjing.vms.utils.UiUtils;

import java.util.List;

/**
 * Created by Jacky on 2016/2/22.
 * Version 1.0
 */
public class VehicleAdapter extends CustomBaseAdapter<Vehicle> {


    public VehicleAdapter(Context context, List<Vehicle> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_vehicle, null);
            holder.tvPlateLicense = (TextView) convertView.findViewById(R.id.tv_plate_license);
            holder.tvTrafficType = (TextView) convertView.findViewById(R.id.tv_traffic_type);
            holder.tvExpireDate = (TextView) convertView.findViewById(R.id.tv_expire_date);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Vehicle vehicle = getList().get(position);
        holder.tvPlateLicense.setText("车牌号码：" + vehicle.getVehiclePlateLicense());
        holder.tvTrafficType.setText("通行类型：" + UiUtils.getVehicleTrafficType(vehicle.getVehicleTrafficType()) );
        holder.tvExpireDate.setText("截至有效期：" + DateUtils.formatTime(vehicle.getVehicleExpireDate()));
        holder.tvStatus.setText(vehicle.getVehicleStatusStr());
        return convertView;
    }


    private class ViewHolder {
        private TextView tvPlateLicense;
        private TextView tvTrafficType;
        private TextView tvExpireDate;
        private TextView tvStatus;
    }
}
