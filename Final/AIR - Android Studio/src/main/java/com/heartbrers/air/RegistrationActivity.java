package com.heartbrers.air;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by nrgz on 23-Apr-18.
 */

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Get root view and button that contains 'back' icon
        final View root = findViewById(R.id.registerRootView);
        View backButton = findViewById(R.id.activity_registration_button_back);

        //If we click the button to go back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Finish this activity and go back to previous Login Activity
                finish();
            }
        });

        //Get necessary views
        final Button registerButton = findViewById(R.id.activity_registration_button_register);
        final EditText userNameEdit = findViewById(R.id.edit_text_register_username);
        final EditText passwordEdit = findViewById(R.id.edit_text_register_password);
        final EditText passwordConfirmEdit = findViewById(R.id.edit_text_register_password_confirm);
        final EditText regionEdit = findViewById(R.id.edit_text_register_region);

        //Add listener for Register Button click
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When we click:
                //Get String values from EditText's
                final String passwordFirst = passwordEdit.getText().toString();
                final String passwordSecond = passwordConfirmEdit.getText().toString();
                final String username = userNameEdit.getText().toString();
                final String region = regionEdit.getText().toString();

                //Check different cases
                if(username.length() == 0) {
                    //If username is empty - show error message
                    Snackbar.make(root, "Username cannot be empty", Snackbar.LENGTH_SHORT).show();
                }
                else if(!passwordFirst.equals(passwordSecond)) {
                    //If passwords do not match - show error message
                    Snackbar.make(root, "Passwords do not match", Snackbar.LENGTH_SHORT).show();
                }
                else if(passwordFirst.length() < 6) {
                    //If password is too short (shorter than 5 chars) - show error message
                    Snackbar.make(root, "Password is too short", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    //Otherwise - everything is good and show message that we are registering
                    Snackbar.make(root, "Registration in process, please, wait", Snackbar.LENGTH_SHORT).show();

                    //Get instance of Firebase Authentictaion
                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    //Create new user with given username and password

                    //Note: because FirebaseAuth requires email addresses, but we are using usernames,
                    //      we add @heartbrers.com to the end of username to mimic email address
                    auth.createUserWithEmailAndPassword(username + "@heartbrers.com", passwordFirst)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //If we have successfully created new account
                                    if(task.isSuccessful() && task.isComplete()) {
                                        //Get SharedPreferences to save variables to phone memory
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
                                        prefs.edit()
                                                .putString("username", username) //Save username
                                                .putString("password", passwordFirst) //Save password
                                                .putBoolean("loggedIn", true) //Set this variable to true if user will not need to log in again when he launches app next time
                                                .apply();

                                        //Get database instance
                                        FirebaseDatabase db = FirebaseDatabase.getInstance();

                                        //Get this user address
                                        DatabaseReference refDatabase = db.getReference().child("users").child(username);

                                        //Set values - Region and amount of coins on start
                                        refDatabase.child("region").setValue(region);
                                        refDatabase.child("coins").setValue(100).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                //If it was successful
                                                if(task.isSuccessful()) {
                                                    //Move to Main Activity
                                                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finishAfterTransition();
                                                }
                                                else if(!task.isComplete()) {
                                                    //If there was no connection - display error message
                                                    Snackbar.make(root, "No connection to server", Snackbar.LENGTH_SHORT).show();
                                                }
                                                else if(!task.isSuccessful()) {
                                                    //This message generally should not trigger, but in some rare occasions it could - display message if this happens
                                                    Snackbar.make(root, "Something went wrong. Please, try again later", Snackbar.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                    else if(!task.isComplete()) {
                                        //If there was no connection - display error message
                                        Snackbar.make(root, "No connection to server", Snackbar.LENGTH_SHORT).show();
                                    }
                                    else if(!task.isSuccessful()) {
                                        //This message generally should not trigger, but in some rare occasions it could - display message if this happens
                                        Snackbar.make(root, "Something went wrong. Please, try again later", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}
