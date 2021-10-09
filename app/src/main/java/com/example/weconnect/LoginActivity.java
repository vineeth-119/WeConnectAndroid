package com.example.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText emailID, password;
    Button btnsignin;
    TextView tvsignup;
    FirebaseAuth mFirebaseAuth;
    private  FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeUI();
        loginUser();
       // new Thread(r).start();
    }
    private void loginUser(){

              mAuthStateListener = new FirebaseAuth.AuthStateListener() {
              final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

              @Override
              public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                  if (mFirebaseUser != null) {
                      Toast.makeText(LoginActivity.this, "You have logged in", Toast.LENGTH_SHORT).show();
                      Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                      startActivity(i);
                  } else {
                      Toast.makeText(LoginActivity.this, "Please Login ", Toast.LENGTH_SHORT).show();
                  }
              }
          };

          btnsignin.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String email = emailID.getText().toString();
                  String pwd = password.getText().toString();
                  if (email.isEmpty()) {
                      emailID.setError("Please Enter EMailID");
                      emailID.requestFocus();
                  } else if (pwd.isEmpty()) {
                      password.setError("Please Enter Password");
                      password.requestFocus();
                  } else if (email.isEmpty() && pwd.isEmpty()) {
                      Toast.makeText(LoginActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
                  } else if (!email.isEmpty() && !pwd.isEmpty()) {
                      mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                          @Override
                          public void onComplete(@NonNull Task<AuthResult> task) {
                              if (!task.isSuccessful()) {
                                  Toast.makeText(LoginActivity.this, "Login Error, Please Login Again!!", Toast.LENGTH_SHORT).show();
                              } else {
                                  Toast.makeText(LoginActivity.this, "Login Success!!", Toast.LENGTH_SHORT).show();
                                  Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                  startActivity(i);
                              }
                          }
                      });
                  } else {
                      Toast.makeText(LoginActivity.this, "Error Occurred!!!", Toast.LENGTH_SHORT).show();
                  }
              }
          });

          tvsignup.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent singup = new Intent(LoginActivity.this, MainActivity.class);
                  startActivity(singup);
              }
          });
        }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void initializeUI() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.email);
        password = findViewById(R.id.pwd);
        btnsignin = findViewById(R.id.button1);
        tvsignup = findViewById(R.id.tv1);
    }
}