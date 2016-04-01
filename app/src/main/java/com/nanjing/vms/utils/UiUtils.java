package com.nanjing.vms.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jacky on 2016/2/21.
 * Version 1.0
 */
public class UiUtils {



    public static final Map<String,String> TRAFFIC_TYPE = new HashMap<>();
    static {
        TRAFFIC_TYPE.put("3","临时通行");
    }

    public static String getVehicleTrafficType(int type) {
        String typeString = "";
        switch (type) {
            case 3:
                typeString = "临时通行";
                break;
            case 4:
                typeString = "会议通行";
                break;
            case 5:
                typeString = "加班通行";
                break;
            case 6:
                typeString = "值班通行";
                break;
            case 7:
                typeString = "应急通行";
                break;
            case 8:
                typeString = "正常通行";
                break;
            default:
                typeString = "禁止通行";
                break;
        }
        return typeString;
    }



    public static String getEmpType(int type) {
        String statusString = "";
        switch (type) {
            case 0:
                statusString = "内部";
                break;
            case 2:
                statusString = "临时";
                break;
            case 3:
                statusString = "短期";
                break;


        }
        return statusString;
    }

}
