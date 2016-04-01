package com.nanjing.vms.network.resp;

import com.nanjing.vms.model.Vehicle;

import java.util.List;

/**
 * Created by Jacky on 2016/3/7.
 * Version 1.0
 */
public class ListVehiclesResp extends BaseResp{

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

   public class Data{
        private List<Vehicle> vehicles;

        public List<Vehicle> getVehicles() {
            return vehicles;
        }

        public void setVehicles(List<Vehicle> vehicles) {
            this.vehicles = vehicles;
        }
    }



}
