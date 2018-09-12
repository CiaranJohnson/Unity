package com.example.ciaranjohnson.weunite;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ciaranjohnson.weunite.Model.Help;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewOffersActivity extends AppCompatActivity {

    private static final String TAG = "ViewOffersActivity";

    //variables
    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<String> mHelpNum = new ArrayList<>();

    //Firebase variables
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offers);
        Log.d(TAG, "onCreate: has started.");

        //initialise Firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();

        initImageBitmap();

    }

    private void initImageBitmap(){
        Log.d(TAG, "initImageBitmap: preparing bitmaps.");

        //get all the name and images and add them to the arraylists.

        mRef = database.getReference("Help");

        mRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //get all the users posts.
                for(DataSnapshot requests: dataSnapshot.getChildren()){
                    Help help = requests.getValue(Help.class);
                    mTitles.add(help.getTitle());
                    mHelpNum.add(requests.getKey());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //think about how you are going to display no help requests for this user
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mTitles, mHelpNum,this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
