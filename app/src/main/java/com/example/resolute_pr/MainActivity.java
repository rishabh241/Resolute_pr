package com.example.resolute_pr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase db;
    Button btn,sign_btn;
   TextView encode,name1;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference dbr;
    String user_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        dbr = db.getReference();
        encode = findViewById(R.id.text);
        btn = findViewById(R.id.btn);
        name1 = findViewById(R.id.name1);
        sign_btn = findViewById(R.id.sign_out);

        user = mAuth.getCurrentUser();
        user_key = user.getUid();
        dbr.child("Users").child(user_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                name1.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        name1.setText();
        dbr = FirebaseDatabase.getInstance().getReference("qrData");
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(),log_in.class);
                startActivity(intent);
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);

                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.setPrompt("scan QR");
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.initiateScan();

            }
        });
//        onActivityResult();
//        db.getReference().child("data").setValue(encode);

    }
//    public void process(View view){
//
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            String resultData = result.getContents();
            if(resultData!=null){
                encode.setText(resultData);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        dbr.push().setValue(encode.getText().toString());
    }



}