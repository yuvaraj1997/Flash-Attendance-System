package com.flash.yuvar.flashattendancesystem.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flash.yuvar.flashattendancesystem.Database.attendance_list_push_qr;
import com.flash.yuvar.flashattendancesystem.QRCode.QRCode_Detail_Activity;
import com.flash.yuvar.flashattendancesystem.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QR_Code_Generator_adapter  extends RecyclerView.Adapter <QR_Code_Generator_adapter.ImageViewHolder>{

    private Context mContext;
    private List<attendance_list_push_qr> mUploads;


    public QR_Code_Generator_adapter(Context context, List<attendance_list_push_qr> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.saved_qr_items, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final attendance_list_push_qr uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getDateandtime());
        Picasso.get()
                .load(uploadCurrent.getUri())
                .fit()
                .centerInside()
                .into(holder.imageView);




        //Intent intent = new Intent(mContext, QRCode_Detail_Activity.class);
        //intent.putExtra("dateandtime", uploadCurrent.getDateandtime());
        //intent.putExtra("image_url", uploadCurrent.getUri());
        //mContext.startActivity(intent);


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView  ) {
            super(itemView);
            itemView.setOnClickListener(this);


            textViewName = itemView.findViewById(R.id.image_View_name);
            imageView = itemView.findViewById(R.id.image_view_upload);






        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            attendance_list_push_qr uploadCurrent = mUploads.get(position);
            Intent intent = new Intent(mContext, QRCode_Detail_Activity.class);



            intent.putExtra("image_url", uploadCurrent.getUri());
            intent.putExtra("dateandtime", uploadCurrent.getDateandtime());

            mContext.startActivity(intent);
        }
    }

}
