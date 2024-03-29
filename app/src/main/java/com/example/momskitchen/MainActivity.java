package com.example.momskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    EditText email, pass;
    Button login;
    TextView signup, forgotPassword;
    TextView error;
    FirebaseAuth auth;
    Button okay;
    String error1, error2, error3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.editTextEmailAddress);
        pass = findViewById(R.id.editTextPassword);
        signup = findViewById(R.id.signUpButton);
        forgotPassword = findViewById(R.id.forgotPasswordButton);
        login = findViewById(R.id.signupButton);
        auth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Password = pass.getText().toString();

                if (TextUtils.isEmpty(Email)){
                    email.setError("Email is missing");
                } else if (TextUtils.isEmpty(Password)) {
                    pass.setError("Password is missing");
                }
                else {
                    auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(auth.getCurrentUser().isEmailVerified()){
                                    startActivity(new Intent(MainActivity.this, MenuActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(MainActivity.this, "Verify your Email address", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                error1="A network error (such as timeout, interrupted connection or unreachable host) has occurred.";
                                error2="The supplied auth credential is incorrect, malformed or has expired.";
                                error3="The email address is badly formatted.";

                                Dialog errorDialog=new Dialog(MainActivity.this);
                                errorDialog.setContentView(R.layout.error_box);

                                error=errorDialog.findViewById(R.id.errorMessage);
                                okay=errorDialog.findViewById(R.id.okay);

                                if(task.getException().getMessage()==error1){
                                    error.setText("CHECK YOUR INTERNET CONNECTION");
                                } else if (task.getException().getMessage()==error2) {
                                    error.setText("INCORRECT INFORMATION");
                                }else if (task.getException().getMessage()==error3) {
                                    error.setText("EMAIL ADDRESS DOES NOT EXIST");
                                }else {
                                    error.setText(task.getException().getMessage());
                                }

                                okay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        errorDialog.dismiss();
                                    }
                                });

                                Log.d("ERROR", task.getException().getMessage());
                                errorDialog.show();
                            }
                        }
                    });
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });
    }
}