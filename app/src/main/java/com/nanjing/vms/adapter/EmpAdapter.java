package com.nanjing.vms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nanjing.vms.R;
import com.nanjing.vms.model.Emp;

import java.util.List;

/**
 * Created by Jacky on 2016/2/22.
 * Version 1.0
 */
public class EmpAdapter extends CustomBaseAdapter<Emp> {

    public EmpAdapter(Context context, List<Emp> list){
        super(context,list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_emp,null);
        }
        return convertView;
    }
}
