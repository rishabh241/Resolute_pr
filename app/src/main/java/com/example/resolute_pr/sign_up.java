package com.example.resolute_pr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity {
TextView log_in;
String username,name,mail,password;
EditText sign_name,sign_username,sign_mail,sign_password;
Button register;
FirebaseDatabase db;
FirebaseAuth mAuth;
DatabaseReference ref;
dataholder data;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        log_in = findViewById(R.id.log_in);
        sign_name = findViewById(R.id.name_input_sign);
        sign_username = findViewById(R.id.username_input_sign);
        sign_mail = findViewById(R.id.email_input_sign);
        sign_password = findViewById(R.id.password_input_sign);
        register = findViewById(R.id.sign_btn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        dialog = new ProgressDialog(sign_up.this);
        dialog.setTitle("Creating Account");
        dialog.setMessage("We're creating your accout");
//        name= sign_name.getText().toString().trim();
//        username = sign_username.getText().toString().trim();
//        mail = sign_mail.getText().toString().trim();
//        password = sign_password.getText().toString().trim();

//        data=new dataholder(name,mail,password);
        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),com.example.resolute_pr.log_in.class);
                startActivity(intent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sign_name.getText().toString().trim().isEmpty() && !sign_username.getText().toString().trim().isEmpty() && !sign_mail.getText().toString().trim().isEmpty() && !sign_password.getText().toString().trim().isEmpty()){
                    dialog.show();
                    mAuth.createUserWithEmailAndPassword(sign_mail.getText().toString().trim(),sign_password.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    dialog.dismiss();
                                    if(task.isSuccessful()){
                                        data=new dataholder(sign_name.getText().toString().trim(),sign_username.getText().toString().trim(),sign_mail.getText().toString().trim(),sign_password.getText().toString().trim());
                                        String id =task.getResult().getUser().getUid();
                                        db.getReference().child("Users").child(id).setValue(data);
                                        db.getReference().child("Users").child(id).setValue(data);
                                        SharedPreferences preferences = getSharedPreferences("credential",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("username",sign_mail.getText().toString().trim());
                                        editor.putString("password",sign_password.getText().toString().trim());
                                        Intent intent = new Intent(sign_up.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(sign_up.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(sign_up.this, "enter credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(sign_up.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
//    public void register(View view){
//
////        ref = db.getReference("user");
////        ref.child(username).setValue(data);
//    }
}