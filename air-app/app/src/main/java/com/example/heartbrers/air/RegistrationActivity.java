package com.example.heartbrers.air;

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
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kkerz on 23-Apr-18.
 */

public class RegistrationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final View root = findViewById(R.id.registerRootView);
        View backButton = findViewById(R.id.activity_registration_button_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button registerButton = findViewById(R.id.activity_registration_button_register);

        final EditText userNameEdit = findViewById(R.id.edit_text_register_username);
        final EditText passwordEdit = findViewById(R.id.edit_text_register_password);
        final EditText passwordConfirmEdit = findViewById(R.id.edit_text_register_password_confirm);
        final EditText regionEdit = findViewById(R.id.edit_text_register_region);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String passwordFirst = passwordEdit.getText().toString();
                final String passwordSecond = passwordConfirmEdit.getText().toString();
                final String username = userNameEdit.getText().toString();
                final String region = regionEdit.getText().toString();

                if(username.length() == 0) {
                    Snackbar.make(root, "Username cannot be empty", Snackbar.LENGTH_SHORT).show();
                }
                else if(!passwordFirst.equals(passwordSecond)) {
                    Snackbar.make(root, "Passwords do not match", Snackbar.LENGTH_SHORT).show();
                }
                else if(passwordFirst.length() < 6) {
                    Snackbar.make(root, "Password is too short", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(root, "Registration in process, please, wait", Snackbar.LENGTH_SHORT).show();
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(username + "@heartbrers.com", passwordFirst)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful() && task.isComplete()) {
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
                                        prefs.edit()
                                                .putString("username", username)
                                                .putString("password", passwordFirst)
                                                .putBoolean("loggedIn", true)
                                                .apply();

                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        db.getReference().child("users").child(username).child("region").setValue(region).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finishAfterTransition();
                                                }
                                                else if(!task.isComplete()) {
                                                    Snackbar.make(root, "No connection to server", Snackbar.LENGTH_SHORT).show();
                                                }
                                                else if(!task.isSuccessful()) {
                                                    Snackbar.make(root, "Something went wrong. Please, try again later", Snackbar.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                    else if(!task.isComplete()) {
                                        Snackbar.make(root, "No connection to server", Snackbar.LENGTH_SHORT).show();
                                    }
                                    else if(!task.isSuccessful()) {
                                        Snackbar.make(root, "Something went wrong. Please, try again later", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}
