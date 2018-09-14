package com.example.ciaranjohnson.weunite;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ciaranjohnson.weunite.Model.Help;
import com.example.ciaranjohnson.weunite.Model.Offers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectUserToHelpActivity extends AppCompatActivity {

    private static final String TAG = "SelectUserHelpActivity";

    //variables
    private ArrayList<String> mUsername = new ArrayList<>();
    private ArrayList<String> mOffersID = new ArrayList<>();
    private String helpNum;

    //Firebase variables
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_to_help);
        Log.d(TAG, "onCreate: has started.");

        Bundle bundle = getIntent().getExtras();
        helpNum = bundle.getString("helpNum");
        Log.d(TAG, "onCreate: " + helpNum);

        //initialise Firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();

        initImageBitmap();

    }

    private void initImageBitmap(){
        Log.d(TAG, "initImageBitmap: preparing bitmaps.");

        //get all the name and images and add them to the arraylists.

        mRef = database.getReference("Offers");

        //add the help offer here
        mRef.child(user.getUid()).child(helpNum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //get all the users posts.
                for(DataSnapshot requests: dataSnapshot.getChildren()){
                    Offers offers = requests.getValue(Offers.class);
                    mUsername.add(offers.getUsername());
                    mOffersID.add(offers.getOffersID());
                    Log.d(TAG, "onDataChange: "+ offers.getUsername() + " " + offers.getOffersID());

                }
                Log.d(TAG, "onDataChange: done");
                initRecyclerView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //think about how you are going to display no help requests for this user
            }
        });

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: started");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        UserRVAdapter userRVAdapter = new UserRVAdapter(mUsername, mOffersID,this);
        recyclerView.setAdapter(userRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
