package com.example.ciaranjohnson.weunite;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ciaranjohnson.weunite.Common.Common;
import com.example.ciaranjohnson.weunite.Model.Help;
import com.example.ciaranjohnson.weunite.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

        MaterialEditText etEmail, etPassword;
        Button signInButton, backButton;

        public static String TAG = "SignIn";

        public User user;
        Common common;
        private FirebaseAuth mAuth;
        private FirebaseDatabase database;
        private DatabaseReference mRef;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_in);

            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            mRef = database.getReference("User");

            etEmail = (MaterialEditText) findViewById(R.id.edtEmail);
            etPassword = (MaterialEditText) findViewById(R.id.edtPassword);

            signInButton = (Button) findViewById(R.id.signInButton);
            backButton = (Button) findViewById(R.id.backButton);


            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                            .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("SIGN_IN", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);


                                        Intent intent = new Intent(SignIn.this, MapsActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("SIGN_IN", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(SignIn.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
//                                        updateUI(null);
                                    }

                                }
                            });
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void updateUI(final FirebaseUser currentUser) {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 user = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                common = new Common();
                common.currentUser = user;
                common.help = new Help();

                Log.d(TAG, user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
