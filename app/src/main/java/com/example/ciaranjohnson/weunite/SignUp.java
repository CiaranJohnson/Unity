package com.example.ciaranjohnson.weunite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.ciaranjohnson.weunite.Common.Common;
import com.example.ciaranjohnson.weunite.Model.User;
import com.facebook.CallbackManager;

import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;



public class SignUp extends AppCompatActivity {

    private static final String TAG = "SignUp";

        MaterialEditText edtEmail, edtName, edtPassword;
        Button signUpButton, signInButton;

        private DatabaseReference mRef;
        private  FirebaseDatabase database;
        private FirebaseStorage storage;
        private StorageReference storageReference;
        private FirebaseAuth.AuthStateListener mAuthListener;

        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);

            mAuth = FirebaseAuth.getInstance();

            database = FirebaseDatabase.getInstance();
            mRef = database.getReference();

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();


            edtName = (MaterialEditText) findViewById(R.id.medtName);
            edtPassword = (MaterialEditText) findViewById(R.id.medtPassword);
            edtEmail = (MaterialEditText) findViewById(R.id.medtEmail);

            signInButton = (Button) findViewById(R.id.signInButton2);
            signUpButton = (Button) findViewById(R.id.signUpButton2);


            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("CREATE_USER", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);

//                                        Intent intent = new Intent(SignUp.this, ProfileActivity.class);
//                                        startActivity(intent);
//                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("CREATE_USER", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUp.this, "Authentication failed. " + task.getException(),
                                                Toast.LENGTH_SHORT).show();
//                                        updateUI(null);
                                    }
                                }

                            });

                }
            });

            addDisplayName();

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    startActivity(intent);
                }
            });



        }

    @Override
    public void onResume(){
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
//        if(mAuthListener != null){
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

//        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser user) {
        User newUser = new User(edtName.getText().toString(), 50, 0, 0);
        newUser.setName(edtName.getText().toString());
        mRef.child("User").child(user.getUid()).setValue(newUser);

        Common.currentUser = newUser;

//        StorageReference profileReference = storageReference.child("images/profile_picture/"+user.getUid());
//        Uri uri = Uri.parse("android.resource://com.example.ciaranjohnson.weunite/drawable/ic_person_outline_black_24dp");
//        UploadTask uploadTask = profileReference.putFile(uri);
//
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e(TAG, "Failure"+e.getMessage());
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//            }
//        });
    }

    public void addDisplayName() {
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(edtName.getText().toString()).build();
                    user.updateProfile(profileUpdates);
                    Intent intent = new Intent(SignUp.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }


}
