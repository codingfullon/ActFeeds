package com.inkeep.actfeeds.signinsignup;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.inkeep.actfeeds.MainActivity;
import com.inkeep.actfeeds.R;

public class SignUp extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvSignUp;
    private TextView tvSignUpGoogle;
    private FirebaseAuth mAuth;
    private String name;
    private String email;
    private String password;
    private static final String TAG = "SignUpActivity";
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize variables
        etName = (EditText) findViewById(R.id.etName) ;
        etEmail = (EditText) findViewById(R.id.etEmail) ;
        etPassword = (EditText) findViewById(R.id.etPassword) ;
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvSignUpGoogle = (TextView) findViewById(R.id.tvSignUpGoogle);
        tvLogin = (TextView) findViewById(R.id.tvLogin);

        mAuth = FirebaseAuth.getInstance();

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });
       // findViewById(R.id.tvSignUpGoogle).setOnClickListener(this);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(login);
            }
        });

    }



    private void registerUser() {
        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if(name.isEmpty()){
            etName.setError("Name is Required");
            etName.requestFocus();
            return;
        }

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
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
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

}
