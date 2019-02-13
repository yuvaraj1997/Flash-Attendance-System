package com.flash.yuvar.flashattendancesystem.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.Database.TableModal;
import com.flash.yuvar.flashattendancesystem.R;

import java.util.List;

public class TableViewAdapter extends RecyclerView.Adapter {
    List<TableModal> studentList;

    public TableViewAdapter(List<TableModal> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.table_list_item, parent, false);

        return new RowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {
            // Header Cells. Main Headings appear here
            rowViewHolder.txtRank.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtStudentName.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtDay1.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtDay2.setBackgroundResource(R.drawable.table_header_cell_bg);

            rowViewHolder.txtRank.setText("Rank");
            rowViewHolder.txtStudentName.setText("Student Name");
            rowViewHolder.txtDay1.setText("Day 1");
            rowViewHolder.txtDay2.setText("Day 2");
            rowViewHolder.txtDay3.setText("Day 3");
            rowViewHolder.txtDay4.setText("Day 4");
            rowViewHolder.txtDay5.setText("Day 5");
            rowViewHolder.txtDay6.setText("Day 6");
            rowViewHolder.txtDay7.setText("Day 7");
            rowViewHolder.txtDay8.setText("Day 8");
            rowViewHolder.txtDay9.setText("Day 9");
            rowViewHolder.txtDay10.setText("Day 10");
            rowViewHolder.txtDay11.setText("Day 11");
            rowViewHolder.txtDay12.setText("Day 12");
            rowViewHolder.txtDay13.setText("Day 13");
            rowViewHolder.txtDay14.setText("Day 14");
            rowViewHolder.txtDay15.setText("Day 15");
            rowViewHolder.txtDay16.setText("Day 16");
            rowViewHolder.txtDay17.setText("Day 17");
            rowViewHolder.txtDay18.setText("Day 18");
            rowViewHolder.txtDay19.setText("Day 19");
            rowViewHolder.txtDay20.setText("Day 20");

        } else {
            TableModal modal = studentList.get(rowPos-1);

            // Content Cells. Content appear here
            rowViewHolder.txtRank.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtStudentName.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtDay1.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtDay2.setBackgroundResource(R.drawable.table_content_cell_bg);

            rowViewHolder.txtRank.setText(modal.getRank()+"");
            rowViewHolder.txtStudentName.setText(modal.getName());
            rowViewHolder.txtDay1.setText(modal.getDate1()+"");
            rowViewHolder.txtDay2.setText(modal.getDate2());
            rowViewHolder.txtDay3.setText("");
            rowViewHolder.txtDay4.setText("");
            rowViewHolder.txtDay5.setText("");
            rowViewHolder.txtDay6.setText("");
            rowViewHolder.txtDay7.setText("");
            rowViewHolder.txtDay8.setText("");
            rowViewHolder.txtDay9.setText("");
            rowViewHolder.txtDay10.setText("");
            rowViewHolder.txtDay11.setText("");
            rowViewHolder.txtDay12.setText("");
            rowViewHolder.txtDay13.setText("");
            rowViewHolder.txtDay14.setText("");
            rowViewHolder.txtDay15.setText("");
            rowViewHolder.txtDay16.setText("");
            rowViewHolder.txtDay17.setText("");
            rowViewHolder.txtDay18.setText("");
            rowViewHolder.txtDay19.setText("");
            rowViewHolder.txtDay20.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size()+1; // one more to add header row
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtRank;
        protected TextView txtStudentName;
        protected TextView txtDay1;
        protected TextView txtDay2;
        protected TextView txtDay3;
        protected TextView txtDay4;
        protected TextView txtDay5;
        protected TextView txtDay6;
        protected TextView txtDay7;
        protected TextView txtDay8;
        protected TextView txtDay9;
        protected TextView txtDay10;
        protected TextView txtDay11;
        protected TextView txtDay12;
        protected TextView txtDay13;
        protected TextView txtDay14;
        protected TextView txtDay15;
        protected TextView txtDay16;
        protected TextView txtDay17;
        protected TextView txtDay18;
        protected TextView txtDay19;
        protected TextView txtDay20;

        public RowViewHolder(View itemView) {
            super(itemView);

            txtRank = itemView.findViewById(R.id.txtRank);
            txtStudentName= itemView.findViewById(R.id.txtStudentName);
            txtDay1 = itemView.findViewById(R.id.txtDay1);
            txtDay2 = itemView.findViewById(R.id.txtDay2);
            txtDay3 = itemView.findViewById(R.id.txtDay3);
            txtDay4 = itemView.findViewById(R.id.txtDay4);
            txtDay5 = itemView.findViewById(R.id.txtDay5);
            txtDay6 = itemView.findViewById(R.id.txtDay6);
            txtDay7 = itemView.findViewById(R.id.txtDay7);
            txtDay8 = itemView.findViewById(R.id.txtDay8);
            txtDay9 = itemView.findViewById(R.id.txtDay9);
            txtDay10 = itemView.findViewById(R.id.txtDay10);
            txtDay11= itemView.findViewById(R.id.txtDay11);
            txtDay12 = itemView.findViewById(R.id.txtDay12);
            txtDay13 = itemView.findViewById(R.id.txtDay13);
            txtDay14 = itemView.findViewById(R.id.txtDay14);
            txtDay15 = itemView.findViewById(R.id.txtDay15);
            txtDay16 = itemView.findViewById(R.id.txtDay16);
            txtDay17 = itemView.findViewById(R.id.txtDay17);
            txtDay18 = itemView.findViewById(R.id.txtDay18);
            txtDay19 = itemView.findViewById(R.id.txtDay19);
            txtDay20 = itemView.findViewById(R.id.txtDay20);

        }
    }
}
