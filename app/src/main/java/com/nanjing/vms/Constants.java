package com.nanjing.vms;

/**
 * Created by Jacky on 2016/3/11.
 * Version 1.0
 */
public interface Constants {
    int PAGE_SIZE_DEFAULT = 20;

    /** 审批通过*/
    int VEHICLE_CHECK_THROUGH = 1;
    /** 审批未通过*/
    int VEHICLE_CHECK_NOT_THROUGH = 2;


    public interface VehicleStatus {
        public static final int NORMAL = 0;
        public static final int FORBID = 1;
        public static final int PENDING = 2;
        public static final int DELETE = 3;
    }

    public interface EmpGender {
        public static final int MALE = 0;
        public static final int FEMALE = 1;
    }

    public interface EmpType {
        public static final int TEMPORARY = 2;
        public static final int INTERNAL = 0;
        public static final int SHORT = 3;
    }


    public interface MemberPermission {
        int EMP_QUERY = 0x0001;
        int EMP_CREATE = 0x0002;
        int EMP_UPDATE = 0x0003;
        int EMP_DELETE = 0x0004;
        int EMP_BLACK = 0x0005;
        int EMP_CHECK = 0x0006;
        int VEHICLE_QUERY = 0x1001;
        int VEHICLE_CREATE = 0x1002;
        int VEHICLE_UPDATE = 0x1003;
        int VEHICLE_DELETE = 0x1004;
        int VEHICLE_BLACK = 0x1005;
        int VEHICLE_CHECK = 0x1006;
    }


    public interface MemberType extends MemberPermission {

        int TYPE_SUPER_ADMIN = 3;
        int TYPE_FRONT_OPERATOR = 4;
        int TYPE_BACK_OPERATOR = 5;
        int TYPE_DATA_AMDIN = 7;
        int TYPE_CHECK_ADMIN = 6;
        int TYPE_BACK_SHIFT_OPERATOR = 8;

        int[] SUPER_ADMIN_PERMISSIONS = {EMP_QUERY, EMP_CREATE, EMP_UPDATE, EMP_DELETE, EMP_BLACK, EMP_CHECK,
                VEHICLE_QUERY, VEHICLE_CREATE, VEHICLE_UPDATE, VEHICLE_DELETE, VEHICLE_BLACK, VEHICLE_CHECK};
        int[] FRONT_OPERATOR_PERMISSIONS = {EMP_QUERY, VEHICLE_QUERY, VEHICLE_CREATE, VEHICLE_UPDATE};
        int[] BACK_OPERATOR_PERMISSIONS = {EMP_QUERY, EMP_CREATE, EMP_UPDATE, VEHICLE_QUERY, VEHICLE_CREATE, VEHICLE_UPDATE};
        int[] CHECK_ADMIN_PERMISSIONS = {EMP_QUERY, EMP_CHECK, VEHICLE_QUERY, VEHICLE_CHECK};
        int[] DATA_ADMIN_PERMISSIONS = {EMP_QUERY, EMP_CREATE, EMP_UPDATE, EMP_DELETE, EMP_BLACK, VEHICLE_QUERY, VEHICLE_CREATE,
                VEHICLE_UPDATE, VEHICLE_DELETE, VEHICLE_BLACK};
        int[] BACK_SHIFT_OPERATOR_PERMISSIONS = {EMP_QUERY, VEHICLE_QUERY};
    }
}
