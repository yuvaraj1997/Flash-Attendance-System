package com.flash.yuvar.flashattendancesystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.flash.yuvar.flashattendancesystem.Admin.StudentSide.Admin_student_details;
import com.flash.yuvar.flashattendancesystem.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {




    private String[] groupnames;
    private List<String> year;
    private String[][] childnames = new String[999][999];

    private Context ctx;
    private String coursename;


    public ExpandableListViewAdapter(final Context ctx, final String coursename, final Integer count, List<String> year){
        this.ctx = ctx;
        this.coursename = coursename;
        groupnames = new String[count];








        int x =0;

        for(int i =0; i<count;i++){
            groupnames[i] = year.get(i);

            DatabaseReference child = FirebaseDatabase.getInstance().getReference("users");

            Integer positio = year.get(i).indexOf(" ");

            String originalyear = year.get(i).substring(positio+1,year.get(i).length());

            final int finalI = i;
            child.orderByChild("year").equalTo(originalyear).addListenerForSingleValueEvent(new ValueEventListener() {
                Integer add =0;
                Integer newadd =0;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ss : dataSnapshot.getChildren()){
                        add++;
                    }



                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        UserProfile student = ds.getValue(UserProfile.class);

                        if(student.getCourse().compareTo(coursename)==0){
                            childnames[finalI][newadd] = student.getName();
                            newadd++;
                        }


                        //Toast.makeText(ctx,student.getName(),Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }




        //Toast.makeText(ctx, Integer.toString(x),Toast.LENGTH_LONG).show();








    }




    @Override
    public int getGroupCount() {
        return groupnames.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childnames[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return groupnames[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childnames[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView txtview = new TextView(ctx);
        txtview.setText(groupnames[groupPosition]);
        txtview.setPadding(100,0,0,0);
        txtview.setTextColor(Color.BLUE);
        txtview.setTextSize(40);
        return txtview;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final TextView view = new TextView(ctx);
        view.setText(childnames[groupPosition][childPosition]);
        view.setPadding(100,0,0,0);
        view.setTextColor(Color.RED);
        view.setTextSize(30);



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx,view.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent i = new Intent (ctx, Admin_student_details.class);
                i.putExtra ("studentname",view.getText().toString());

                ctx.startActivity(i);
                return false;
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
