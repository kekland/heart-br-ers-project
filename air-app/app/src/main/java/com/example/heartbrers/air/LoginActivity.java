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

/**
 * Created by nrgz on 22.04.2018.
 */

public class LoginActivity extends AppCompatActivity {

    EditText userNameInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("loggedIn", false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finishAfterTransition();
            return;
        }
        final View root = findViewById(R.id.loginActivityRoot);


        Button signInButton = findViewById(R.id.button_sign_in);
        userNameInput = findViewById(R.id.edit_text_login_username);
        passwordInput = findViewById(R.id.edit_text_sign_in_password);

        Button registerButton = findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                final String username = userNameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                if(username.length() == 0 || password.length() == 0) {
                    Snackbar.make(root, "Incorrect username or password", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                Snackbar.make(root, "Login in process, please, wait", Snackbar.LENGTH_SHORT).show();

                auth.signInWithEmailAndPassword(username + "@heartbrers.com", password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isComplete() && task.isSuccessful()) {
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                    prefs.edit()
                                            .putString("username", username)
                                            .putString("password", password)
                                            .putBoolean("loggedIn", true)
                                            .apply();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finishAfterTransition();
                                }
                                else if(!task.isComplete()) {
                                    Snackbar.make(root, "No connection to server", Snackbar.LENGTH_SHORT).show();
                                }
                                else if(!task.isSuccessful()) {
                                    Snackbar.make(root, "Incorrect username or password", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
