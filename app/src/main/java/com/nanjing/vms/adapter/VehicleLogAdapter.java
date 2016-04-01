package com.nanjing.vms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanjing.vms.R;
import com.nanjing.vms.model.VehicleLog;
import com.nanjing.vms.utils.DateUtils;
import com.nanjing.vms.utils.UiUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Jacky on 2016/2/22.
 * Version 1.0
 */
public class VehicleLogAdapter extends CustomBaseAdapter <VehicleLog>{

    public VehicleLogAdapter(Context context, List<VehicleLog> list) {
        super(context,list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder = null;
        if (convertView == null) {
            holder  = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_vehicle_logs, null);
            holder.tv_plate_license = (TextView) convertView.findViewById(R.id.tv_plate_license);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_gateway_name = (TextView) convertView.findViewById(R.id.tv_gateway_name);
            holder.tv_traffic_type = (TextView) convertView.findViewById(R.id.tv_traffic_type);
            holder.tv_order = (TextView) convertView.findViewById(R.id.tv_order);
            holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            holder.iv_plate_pic = (ImageView) convertView.findViewById(R.id.iv_plate_pic);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        VehicleLog vehicleLog = getList().get(position);
        holder.tv_plate_license.setText("车牌号码："+vehicleLog.getVehiclePlateLicense());
        holder.tv_time.setText("通行时间："+DateUtils.formatTime(vehicleLog.getVehicleLogAbsTime()));
        holder.tv_gateway_name.setText("通行地点："+vehicleLog.getVehicleLogGatewayName());
        holder.tv_traffic_type.setText("通行类型："+UiUtils.getVehicleTrafficType(Integer.parseInt(vehicleLog.getVehicleTrafficType())));
        holder.tv_order.setText(vehicleLog.getVehicleLogCameraOrder());
        ImageLoader.getInstance().displayImage(vehicleLog.getVehicleLogPicName(), holder.iv_pic);
        ImageLoader.getInstance().displayImage(vehicleLog.getVehicleLogPlatePicName(),holder.iv_plate_pic);
        return convertView;
    }


    class ViewHolder {
        private TextView tv_plate_license;
        private TextView tv_time;
        private TextView tv_traffic_type;
        private TextView tv_gateway_name;
        private TextView tv_order;
        private ImageView iv_pic,iv_plate_pic;
    }
}
