package com.flash.yuvar.flashattendancesystem.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.Database.student_registered_list;
import com.flash.yuvar.flashattendancesystem.R;

import java.util.List;

public class student_percentage_adapter extends RecyclerView.Adapter<student_percentage_adapter.MyViewHolder> {


    List<student_registered_list> requestList;
    private Context ctx;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView student_name,student_percentage;
        public Button but_send_Warning;

        public MyViewHolder(View view) {
            super(view);
            but_send_Warning= (Button) view.findViewById(R.id.but_send_warning_issue);
            student_name = (TextView) view.findViewById(R.id.student_name_percentage);
            student_percentage = (TextView) view.findViewById(R.id.student_percentage);



        }
    }

    public student_percentage_adapter(List<student_registered_list> requestList) {



        this.requestList = requestList;
    }


    @Override
    public student_percentage_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_percentage, parent, false);

        this.ctx = parent.getContext ();

        return new student_percentage_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final student_percentage_adapter.MyViewHolder holder, final int position) {

        final student_registered_list list = requestList.get(position);




        holder.student_name.setText(list.getName().trim());


        //holder.student_percentage.setText(list.getCount1().toString() + "%");
        holder.student_percentage.setText(String.format("%.2f ", list.getCount1())+"%");








    }




        @Override
    public int getItemCount() {

        return requestList.size();
    }

}
