package com.nanjing.vms.network.resp;

import com.nanjing.vms.model.EmpLog;

import java.util.List;

/**
 * Created by Jacky on 2016/3/7.
 * Version 1.0
 */
public class ListEmpLogsResp extends BaseResp{

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    class Data{
        private List<EmpLog> EmpLogs;

        public List<EmpLog> getEmpLogs() {
            return EmpLogs;
        }

        public void setEmpLogs(List<EmpLog> vehicles) {
            this.EmpLogs = vehicles;
        }
    }



}
