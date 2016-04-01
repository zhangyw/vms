package com.nanjing.vms.network.resp;

import com.nanjing.vms.model.Emp;

import java.util.List;

/**
 * Created by Jacky on 2016/3/7.
 * Version 1.0
 */
public class ListEmpsResp extends BaseResp{

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private List<Emp> Emps;

        public List<Emp> getEmps() {
            return Emps;
        }

        public void setEmps(List<Emp> emps) {
            this.Emps = emps;
        }
    }



}
