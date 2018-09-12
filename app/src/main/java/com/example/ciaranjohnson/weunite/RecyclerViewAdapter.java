package com.example.ciaranjohnson.weunite;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mHelpNum = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mImageNames, ArrayList<String> mHelpNum, Context mContext) {
        this.mImageNames = mImageNames;
        this.mHelpNum = mHelpNum;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

//        Glide.with(mContext).asBitmap().load(mImages.get(position)).into(viewHolder.image);
        viewHolder.imageName.setText(mImageNames.get(position));

        viewHolder.offerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on"+ mImageNames.get(position));
//                Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_LONG).show();
                Toast.makeText(mContext, mHelpNum.get(position), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

//        CircleImageView image;
        TextView imageName;
        RelativeLayout offerLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            image = (CircleImageView) itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            offerLayout = itemView.findViewById(R.id.offer_layout);
        }
    }
}
