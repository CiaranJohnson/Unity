package com.example.ciaranjohnson.weunite;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ciaranjohnson.weunite.Model.Offers;
import com.example.ciaranjohnson.weunite.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

public class SelectChatActivity extends AppCompatActivity {

    private static final String TAG = "SelectChatActivity";

    //variables
    private ArrayList<String> mRecipientsID = new ArrayList<>();
    private ArrayList<String> mUsername = new ArrayList<>();
    private String helpNum;

    //Firebase variables
    private FirebaseDatabase database;
    private FirebaseDatabase myDatabase;
    private DatabaseReference mRef;
    private DatabaseReference mRefenence;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_chat);
        Log.d(TAG, "onCreate: has started.");

        //initialise Firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        myDatabase = FirebaseDatabase.getInstance();

        initImageBitmap();
    }



    private void initImageBitmap(){
        Log.d(TAG, "initImageBitmap: preparing bitmaps.");
        //get all the name and images and add them to the arraylists.


        //add the help offer here
        mRef = database.getReference("messages");
        mRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //get all the users posts.
                for(DataSnapshot requests: dataSnapshot.getChildren()){
                    mRecipientsID.add(requests.getKey());
                    Log.d(TAG, "onDataChange: "+ requests.getKey());

                    //Get the display names of the people the user is chatting to.
//                    retrieveName(requests.getKey());

                }
                for(String id: mRecipientsID) {
                    Log.d(TAG, "onDataChange: inside the loop");
                    retrieveName(id);
                }

                Log.d(TAG, "onDataChange: done");
                Log.d(TAG, "onDataChange: "+ mUsername.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //think about how you are going to display no help requests for this user
                Log.e(TAG, "onCancelled: " + databaseError.getDetails());
            }
        });

    }

    private void retrieveName(String id){
        mRefenence = myDatabase.getReference("User");
        mRefenence.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mUsername.add(user.getName());

                if(mUsername.size() == mRecipientsID.size()){
                    initRecyclerView();
                }

                Log.d(TAG, "onDataChange: " + user.getName());
                Log.d(TAG, "onDataChange: " + mUsername.toString());
                return;

//                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getDetails());
            }
        });
    }


    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: started");
        RecyclerView recyclerView = findViewById(R.id.chat_recycler_view);
        MessangerRVAdapter messangerRVAdapter = new MessangerRVAdapter(mRecipientsID, mUsername, this);
        recyclerView.setAdapter(messangerRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}


