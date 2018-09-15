package com.example.ciaranjohnson.weunite;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessangerRVAdapter extends RecyclerView.Adapter<MessangerRVAdapter.ViewHolder>{


    private static final String TAG = "UserRVAdapter";

    private ArrayList<String> mRecipientsID = new ArrayList<>();
    private ArrayList<String> mUsername = new ArrayList<>();
    private Context mContext;

    private StorageReference storageReference;
    private FirebaseStorage storage;

    public MessangerRVAdapter(ArrayList<String> mRecipientsID, ArrayList<String> mUsername, Context mContext) {
        this.mRecipientsID = mRecipientsID;
        this.mUsername = mUsername;
        this.mContext = mContext;

        Log.d(TAG, "MessangerRVAdapter: " + mUsername.toString());
    }

    @NonNull
    @Override
    public MessangerRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_chat_list_item, parent, false);
        MessangerRVAdapter.ViewHolder holder = new MessangerRVAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessangerRVAdapter.ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

//      Glide.with(mContext).asBitmap().load(mOffersID.get(position)).into(viewHolder.image);
        viewHolder.username.setText(mUsername.get(position));
        Log.d(TAG, "onBindViewHolder: " + mUsername.get(position));

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        storageReference.child("images/profile_picture/" + mRecipientsID.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) { Picasso.with(mContext).load(uri).into(viewHolder.image);

            }
        }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, "Failed: Profile Picture");
                }
        });

        viewHolder.chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Need to add some code so that the firebase stores the user selected.
                Log.d(TAG, "onClick: clicked on"+ mUsername.get(position));
                Toast.makeText(mContext, mUsername.get(position), Toast.LENGTH_LONG).show();


                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("id", mRecipientsID.get(position));
                mContext.startActivity(intent);

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
        RelativeLayout chatLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (CircleImageView) itemView.findViewById(R.id.image);
            username = itemView.findViewById(R.id.username);
            chatLayout = itemView.findViewById(R.id.chat_name_layout);
        }
    }

}
