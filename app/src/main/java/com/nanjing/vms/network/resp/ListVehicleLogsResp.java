package com.nanjing.vms.network.resp;

import com.nanjing.vms.model.VehicleLog;

import java.util.List;

/**
 * Created by Jacky on 2016/3/7.
 * Version 1.0
 */
public class ListVehicleLogsResp extends BaseResp{

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

   public class Data{
        private List<VehicleLog> VehicleLogs;

        public List<VehicleLog> getVehicles() {
            return VehicleLogs;
        }

        public void setVehicles(List<VehicleLog> vehicles) {
            this.VehicleLogs = vehicles;
        }
    }



}
