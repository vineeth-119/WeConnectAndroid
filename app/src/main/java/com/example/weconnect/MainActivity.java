package com.example.weconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextInputEditText emailID, password;
    Button btnsignup;
    TextView tvsignin;
    FirebaseAuth mFirebaseAuth;
    public String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        initializeUI();
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
        tvsignin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public  void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                });
    }
     private void registerNewUser(){

            String email = emailID.getText().toString();
            String pwd = password.getText().toString();

            if (email.isEmpty()) {
                emailID.setError("Please Enter EMailID");
                emailID.requestFocus();
            } else if (pwd.isEmpty()) {
                password.setError("Please Enter Password");
                password.requestFocus();
            } else if (email.isEmpty() && pwd.isEmpty()) {
                Toast.makeText(MainActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
            } else if (!email.isEmpty() && !pwd.isEmpty()) {
                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "SignUp Unsuccessfull,Please try again", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Please Fill Your Details", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, ProfileEdit.class));
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "Error Occurred!!!", Toast.LENGTH_SHORT).show();
            }
        }

    private void initializeUI() {
        emailID = findViewById(R.id.email);
        password = findViewById(R.id.pwd);
        btnsignup = findViewById(R.id.button1);
        tvsignin = findViewById(R.id.tv1);
    }
}