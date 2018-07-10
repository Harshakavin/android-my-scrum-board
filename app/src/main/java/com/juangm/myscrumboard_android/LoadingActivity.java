package com.juangm.myscrumboard_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


//Primary activity: check if user has saved token and userID and go to login or kanban activity
public class LoadingActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mPrefs = getSharedPreferences("TaskbanPreferences", MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        String userID = mPrefs.getString("userID", "");
        String token = mPrefs.getString("token", "");
        goLoginActivity();
//        if(!userID.equals("") && !token.equals("")) {
//            checkServerStatus();
//        } else {
//
//        }
    }


    //Go to the main activity
    private void goKanbanActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Go to the login activity
    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
