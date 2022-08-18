package com.example.loginwithfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText forgotEmail;
    Button sendRequest;
    FirebaseAuth auth;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotEmail=findViewById(R.id.forgotEmail);
        sendRequest=findViewById(R.id.sendRequest);

        auth=FirebaseAuth.getInstance();

        actionBar=getSupportActionBar();
        actionBar.setTitle("Forgot Password");
        actionBar.setDisplayHomeAsUpEnabled(true);

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forgotEmail.getText().toString().length() == 0){
                    Toast.makeText(ForgotPassword.this,"Please fill the details",Toast.LENGTH_LONG).show();
                }
                else {
                    auth.sendPasswordResetEmail(forgotEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                                Toast.makeText(ForgotPassword.this, "Your request sent to your Registered Email ID.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ForgotPassword.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
