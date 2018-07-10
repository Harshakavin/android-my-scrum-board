package com.juangm.myscrumboard_android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.juangm.myscrumboard_android.models.User;

//Allow the users to enter the account and sign in to the application
public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView registerText;
    private TextInputEditText usernameText;
    private TextInputEditText passwordText;
    private TextInputLayout userInputLayout;
    private TextInputLayout passInputLayout;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginButton = (Button) findViewById(R.id.loginButton);
        usernameText = (TextInputEditText) findViewById(R.id.loginUsername);
        passwordText = (TextInputEditText) findViewById(R.id.loginPassword);
        userInputLayout = (TextInputLayout) findViewById(R.id.userLoginInputLayout);
        passInputLayout = (TextInputLayout) findViewById(R.id.passLoginInputLayout);
        progressDialog = new ProgressDialog(this, R.style.DialogLightTheme); //Light theme dialog
        progressDialog.setTitle(getResources().getString(R.string.login_dialog_title));
        progressDialog.setMessage(getResources().getString(R.string.login_dialog_content));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogLightTheme);
        alertDialog.setTitle(getResources().getString(R.string.login_dialog_error_title));
        alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        prefsEditor = getSharedPreferences("SheardPreferences", MODE_PRIVATE).edit();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If a field is empty, set the error to the edittext
                boolean validateUser = validateUsername();
                boolean validatePass = validatePassword();
               if(validateUser && validatePass) {
                    //If the fields are correct...
                    progressDialog.show();

                   User user= new User(usernameText.getText().toString(),
                            passwordText.getText().toString());

                                prefsEditor.putString("username","sliit");
                                prefsEditor.putString("name","harsha");
                                prefsEditor.putString("email", "harsha@gmail.com");
                                prefsEditor.putString("userID", "001");
                                prefsEditor.apply();
                                progressDialog.dismiss();
                                Log.i("","Login user");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
               } else {
                                //Shows an error message
                                progressDialog.dismiss();
                                alertDialog.setMessage(getResources().getString(R.string.login_dialog_error_content));
                                alertDialog.show();
               }
                        }
        });

    }

    @Override
    protected void onStart() {

        mPrefs = getSharedPreferences("SheardPreferences", MODE_PRIVATE);
        if(mPrefs.getString("username", "").equals("sliit")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        super.onStart();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    //Validate username field
    public boolean validateUsername() {
        if(usernameText.getText().toString().trim().isEmpty()) {
            userInputLayout.setError(getResources().getString(R.string.username_error));
            return false;
        } else if( usernameText.getText().toString().toLowerCase().equals( "sliit")  ){
            userInputLayout.setErrorEnabled(false);
            return true;
        }else{
            userInputLayout.setError("Username is incorrect");
            return false;
        }
    }

    //Validate password field
    public boolean validatePassword() {
        if(passwordText.getText().toString().trim().isEmpty()) {
            passInputLayout.setError(getResources().getString(R.string.password_error));
            return false;
        } else if( passwordText.getText().toString().toLowerCase().equals("abcd1234") ){
            passInputLayout.setErrorEnabled(false);
            return true;
        }else{
            passInputLayout.setError("Password is incorrect");
            return false;
        }
    }
}