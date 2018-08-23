package com.example.ciaranjohnson.weunite;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ciaranjohnson.weunite.Common.Common;
import com.example.ciaranjohnson.weunite.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ciaranjohnson.weunite.Common.Common.currentUser;

public class ProfileActivity extends AppCompatActivity {

    public static String TAG = "ProfileActivity";

    Button buttonHome;
    TextView txtName, txtEmail, txtInvite, txtCoins,txtNumRequests, txtHelped;
    CircleImageView profilePicture;

    User currentUser;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private StorageReference storageReference;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");

        FirebaseUser user = mAuth.getCurrentUser();

        buttonHome = (Button) findViewById(R.id.buttonBack);

        txtInvite = (TextView) findViewById(R.id.txtInvite);
        txtEmail = (TextView) findViewById(R.id.addEmailTxt);
        txtName = (TextView) findViewById(R.id.addNameTxt);
        txtCoins = (TextView) findViewById(R.id.txtCoins);
        txtNumRequests = (TextView) findViewById(R.id.txtNumRequests);
        txtHelped = (TextView) findViewById(R.id.txtHelped);
        profilePicture = (CircleImageView) findViewById(R.id.profilePicture);

//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                currentUser = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
//                txtName.setText(currentUser.getName());
//                txtCoins.setText(Integer.toString(currentUser.getCoins()));
//                txtNumRequests.setText(Integer.toString(currentUser.getNumRequests()));
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        currentUser = Common.currentUser;
        txtName.setText(currentUser.getName());
        txtCoins.setText(Integer.toString(currentUser.getCoins()));
        txtNumRequests.setText(Integer.toString(currentUser.getNumRequests()));

        txtEmail.setText(user.getEmail());



        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        storageReference.child("images/profile_picture/" + user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(ProfileActivity.this).load(uri).into(profilePicture);
                //profilePicture.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "Failed: Profile Picture");
            }
        });



        goToMaps();

    }

    private void updateUI(final FirebaseUser user) {
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                currentUser = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    public void goToMaps(){
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void editProfile(View view){
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);

    }
}
