package com.example.resolute_pr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class log_in extends AppCompatActivity  {
EditText log_email, log_pass;
Button log_btn;
TextView signup;
ProgressDialog dialog;
FirebaseAuth mAuth;
FirebaseDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        log_email = findViewById(R.id.username_input);
        log_pass = findViewById(R.id.password_input);
        log_btn = findViewById(R.id.login_btn);
        signup = findViewById(R.id.sign_up);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        dialog = new ProgressDialog(log_in.this);
        dialog.setTitle("Signing In");
        dialog.setMessage("Please wait\n Validation in process");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),sign_up.class);
                startActivity(intent);
                finish();
            }
        });

        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!log_email.getText().toString().trim().isEmpty() && !log_pass.getText().toString().trim().isEmpty()){
                dialog.show();
                mAuth.signInWithEmailAndPassword(log_email.getText().toString().trim(),log_pass.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                dialog.dismiss();
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(log_in.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(log_in.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                }else {
                Toast.makeText(log_in.this, "Wrong credential", Toast.LENGTH_SHORT).show();

            }
            }
        });
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(log_in.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}