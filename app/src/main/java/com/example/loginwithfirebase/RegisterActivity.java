package com.example.loginwithfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username,password,email,memberid;
    Button signup;
    FirebaseAuth auth;
    ProgressBar progressBar;
    DatabaseReference reference;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=findViewById(R.id.email);
        memberid=findViewById(R.id.memberID);
        username=findViewById(R.id.usernameAddress);
        password=findViewById(R.id.password);
        signup=findViewById(R.id.signup);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Register Email ID");
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressBar=findViewById(R.id.progressBar);

        auth=FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().length() == 0  && password.getText().toString().length() == 0){
                    Toast.makeText(RegisterActivity.this, "Please Enter Details", Toast.LENGTH_LONG).show();
                }
                else {

                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_LONG).show();

                                        FirebaseUser firebaseUser=auth.getCurrentUser();
                                        String UserId=firebaseUser.getUid();

                                        reference= FirebaseDatabase.getInstance().getReference("Users").child(UserId);

                                        HashMap<String,Object> hashMap=new HashMap<>();
                                        hashMap.put("id", UserId);
                                        hashMap.put("imageURL","default");
                                        hashMap.put("username",username.getText().toString().trim());
                                        hashMap.put("memberId",memberid.getText().toString().trim());
                                        hashMap.put("status","offline");
                                        Log.e("Username", username.getText().toString().trim());

                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                 Intent intent=new Intent(RegisterActivity.this,ProfileActivity.class);
                                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                 startActivity(intent);
                                                    username.setText("");
                                                    password.setText("");
                                                    email.setText("");
                                                 finish();
                                                }

                                            }
                                        });

                                    } else {
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
