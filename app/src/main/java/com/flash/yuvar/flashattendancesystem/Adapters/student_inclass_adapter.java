package com.flash.yuvar.flashattendancesystem.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.R;

import java.util.List;

public class student_inclass_adapter extends  RecyclerView.Adapter<student_inclass_adapter.MyViewHolder>  {


    private List<student_registered_list>inclassList;


    private Context ctx;







    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView student_name;


        public MyViewHolder(View view) {
            super(view);

            student_name = (TextView) view.findViewById(R.id.adapter_attendees_list);



        }
    }





    @NonNull
    @Override
    public student_inclass_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_inclass, viewGroup, false);

        this.ctx = viewGroup.getContext ();

        return new student_inclass_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull student_inclass_adapter.MyViewHolder myViewHolder, int i) {

        student_registered_list list =inclassList.get(i);

        myViewHolder.student_name.setText(list.getName());

    }

    @Override
    public int getItemCount() {
        return inclassList.size();
}

    public student_inclass_adapter(List<student_registered_list> inclasslist) {



        this.inclassList = inclasslist;
    }
}
