package com.example.loginwithfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    TextView register,login,forgotPassword;
    EditText userbane,password;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register=findViewById(R.id.register);
        userbane=findViewById(R.id.username);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        forgotPassword=findViewById(R.id.forgotPassword1);

        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();

        if(firebaseUser != null){
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userbane.getText().toString().length() == 0  && password.getText().toString().length() == 0){
                    Toast.makeText(MainActivity.this, "Please Enter Details", Toast.LENGTH_LONG).show();
                }

                 else  {
                    auth.signInWithEmailAndPassword(userbane.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                                        finish();
                                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                }



            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
            }
        });
    }
}
