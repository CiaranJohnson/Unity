package com.example.ciaranjohnson.weunite;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.appinvite.AppInviteInvitation;

public class OptionsActivity extends AppCompatActivity {

    private static final int REQUEST_INVITE = 1;
    private static final String TAG = "OptionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

    public void goToAskForHelp(View view){
        Log.d(TAG, "goToAskForHelp: selected");
        Intent intent = new Intent(OptionsActivity.this, createHelpActivity.class);
        startActivity(intent);
    }

    public void search(View view){
        Log.d(TAG, "search: selected");
        Intent intent = new Intent(OptionsActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void helpOut(View view){
        Log.d(TAG, "opened: ViewOfferActivity");
        
        Intent intent = new Intent(OptionsActivity.this, ViewOffersActivity.class);
        startActivity(intent);

    }


    public void inviteFriend(View view){
        Log.d(TAG, "inviteFriend: selected");
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }



    public void openChat(View view){
        Intent intent = new Intent(OptionsActivity.this, ChatActivity.class);
        startActivity(intent);

    }

    public void settings(View view){
        Intent intent = new Intent(OptionsActivity.this, SettingsActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Check how many invitations were sent and log.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                Log.d(TAG, "Invitations sent: " + ids.length);
            } else {
                // Sending failed or it was canceled, show failure message to the user
                Log.d(TAG, "Failed to send invitation.");
            }
        }
    }
}
