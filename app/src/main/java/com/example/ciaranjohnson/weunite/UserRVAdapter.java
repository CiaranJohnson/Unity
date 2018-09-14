package com.example.ciaranjohnson.weunite;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRVAdapter extends RecyclerView.Adapter<UserRVAdapter.ViewHolder>{

    private static final String TAG = "UserRVAdapter";

    private ArrayList<String> mUsername = new ArrayList<>();
    private ArrayList<String> mOffersID = new ArrayList<>();
    private Context mContext;

    private StorageReference storageReference;
    private FirebaseStorage storage;

    public UserRVAdapter(ArrayList<String> mUsername, ArrayList<String> mOffersID, Context mContext) {
        this.mUsername = mUsername;
        this.mOffersID = mOffersID;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UserRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_names_list_item, parent, false);
        UserRVAdapter.ViewHolder holder = new UserRVAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserRVAdapter.ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

//        Glide.with(mContext).asBitmap().load(mOffersID.get(position)).into(viewHolder.image);
        viewHolder.username.setText(mUsername.get(position));
        Log.d(TAG, "onBindViewHolder: " + mUsername.get(position));

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        storageReference.child("images/profile_picture/" + mOffersID.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(mContext).load(uri).into(viewHolder.image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "Failed: Profile Picture");
            }
        });

        viewHolder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on"+ mUsername.get(position));
                Toast.makeText(mContext, mUsername.get(position), Toast.LENGTH_LONG).show();


            }
        });
    }


    @Override
    public int getItemCount() {
        return mUsername.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView username;
        RelativeLayout offerLayout;
        Button acceptBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            acceptBtn = (Button) itemView.findViewById(R.id.acceptBtn);
            image = (CircleImageView) itemView.findViewById(R.id.image);
            username = itemView.findViewById(R.id.username);
            offerLayout = itemView.findViewById(R.id.offer_layout);
        }
    }
}
