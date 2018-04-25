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

/**
 * Created by nrgz on 22.04.2018.
 */
public class LoginActivity extends AppCompatActivity {

    //EditText objects for username and password
    EditText userNameInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Revert theme from SplashScreen back to AppTheme
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //Get SharedPreferences object to store variables on hard drive
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //If we already have logged in
        if(prefs.getBoolean("loggedIn", false)) {
            //Then skip the Login Activity and move directly to Main Activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finishAfterTransition();
            return;
        }

        //Get root view
        final View root = findViewById(R.id.loginActivityRoot);

        //Get objects
        Button signInButton = findViewById(R.id.button_sign_in);
        userNameInput = findViewById(R.id.edit_text_login_username);
        passwordInput = findViewById(R.id.edit_text_sign_in_password);
        Button registerButton = findViewById(R.id.button_register);

        //When we click on Registration button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Registration Activity
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        //When we click on Sign In button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get instance of Firebase Authentication
                FirebaseAuth auth = FirebaseAuth.getInstance();

                //Get username and password strings
                final String username = userNameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                //If either username or password are empty
                if(username.length() == 0 || password.length() == 0) {
                    //Display error message
                    Snackbar.make(root, "Incorrect username or password", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //Show that we are logging in
                Snackbar.make(root, "Login in process, please, wait", Snackbar.LENGTH_SHORT).show();

                //Call signIn function in Firebase authentication
                auth.signInWithEmailAndPassword(username + "@heartbrers.com", password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //If log in was successful
                                if(task.isComplete() && task.isSuccessful()) {
                                    prefs.edit()
                                            .putString("username", username) //Save username
                                            .putString("password", password) //Save password
                                            .putBoolean("loggedIn", true) //And set that we have logged in
                                            .apply(); //Save data

                                    //Open Main Activity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finishAfterTransition();
                                }
                                else if(!task.isComplete()) {
                                    //If we were not able to connect to server - display error message
                                    Snackbar.make(root, "No connection to server", Snackbar.LENGTH_SHORT).show();
                                }
                                else if(!task.isSuccessful()) {
                                    //If username or password was incorrect - show error message
                                    Snackbar.make(root, "Incorrect username or password", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
