package com.inkeep.actfeeds.signinsignup;
/*Login Page
Input: Email and Password or Using Google Sign In
 */

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.inkeep.actfeeds.MainActivity;
import com.inkeep.actfeeds.R;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPassword;
    private TextView tvSignIn;
    private TextView tvSignInGoogle;
    private TextView tvSignUp;
    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize variables
        etEmail = (EditText) findViewById(R.id.etEmail) ;
        etPassword = (EditText) findViewById(R.id.etPassword) ;
        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
        tvSignInGoogle = (TextView) findViewById(R.id.tvSignInGoogle);


        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.tvSignIn).setOnClickListener(this);
        findViewById(R.id.tvSignInGoogle).setOnClickListener(this);
        findViewById(R.id.tvSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(getBaseContext(), SignUp.class);
                startActivity(signUp);
                finish();
            }
        });

    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tvSignIn:
                loginUser();
                break;

            case R.id.tvSignInGoogle:

                break;

            case R.id.tvSignUP:
                Intent signUp = new Intent(this, SignUp.class);
                startActivity(signUp);
                break;
        }
    }

    private void loginUser() {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError("Email is Required");
            etEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            etPassword.setError("Minimum length should be 6");
            etPassword.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
}
