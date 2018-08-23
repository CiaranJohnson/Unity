package com.example.ciaranjohnson.weunite;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ciaranjohnson.weunite.Common.Common;
import com.example.ciaranjohnson.weunite.Model.Help;
import com.example.ciaranjohnson.weunite.Model.Offers;
import com.example.ciaranjohnson.weunite.Model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class createHelpActivity extends AppCompatActivity {

    public static final String TAG = "createHelpActivty";
    Button addHelpButton;

    EditText etDescription, etReward;

    private DatabaseReference table_help, table_user;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Boolean added;

    private Help newHelp;
    private Offers newOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_help);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        newHelp = new Help();
        added = false;

        addHelpButton = (Button) findViewById(R.id.addHelpButton);

        etDescription = (EditText) findViewById(R.id.etDescription);
        etReward = (EditText) findViewById(R.id.etReward);

        //Creating a instance of the firebase database which stores user information and location information
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        table_user = database.getReference("User");

        /*
         * this method adds all information supplied by the user on the help they require to the database
         * it also gets the users current location when they hit the button and stores it as the help location
         * then gets current user information and checks there is enough coins
         * updates account and stores help under the current user.
         */

        addHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                table_user.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(Integer.parseInt(etReward.getText().toString())>Common.currentUser.getCoins()){
                            Toast.makeText(createHelpActivity.this, "You do not have enough Coins.", Toast.LENGTH_LONG).show();
                        } else if(Integer.parseInt(etReward.getText().toString())<0){
                            Toast.makeText(createHelpActivity.this, "Oops you need to use some coins.", Toast.LENGTH_LONG).show();
                        } else {
                            if(!added){

                                added=true;
                                Double latitude = Common.latLng.latitude;
                                Double longitude = Common.latLng.longitude;

                                newHelp = new Help(firebaseUser.getDisplayName(), etDescription.getText().toString(), latitude, longitude, 0);
                                newOffer = new Offers("No one");
                                Common.help = newHelp;

                                User user = Common.currentUser;


                                int coins = user.getCoins();
                                user.setCoins(coins-Integer.parseInt(etReward.getText().toString()));
                                int requests = user.getNumRequests() + 1;
                                user.setNumRequests(requests);
                                Common.currentUser = user;

                                table_help = database.getReference("Help");


                                table_user.child(firebaseUser.getUid()).setValue(Common.currentUser);
                                table_help.child(firebaseUser.getUid()).child(Integer.toString(Common.currentUser.getNumRequests()))
                                        .setValue(Common.help);



                                Intent intent = new Intent(createHelpActivity.this, MapsActivity.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getDetails());

                    }
                });

            }
        });
    }
}
