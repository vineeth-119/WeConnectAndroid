package com.example.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileView extends AppCompatActivity {

    DatabaseReference dref;
    TextView fullname,phone_number,aadhar_number,age,gender,occupation,h_no, village, district,state,pincode,editProf,email;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        initializeUI();
       // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getData();
        editProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileView.this,ProfileEdit.class);
                startActivity(intent);
            }
        });
    }
     private void getData(){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                dref = FirebaseDatabase.getInstance("https://weconnect-b5543-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Profile").child(user.getUid());
         if(dref==null)
         {
             Toast.makeText(ProfileView.this, "User data not added ",Toast.LENGTH_SHORT).show();
             return;
         }
                dref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String fullname1 = snapshot.child("full_name").getValue().toString();
                        String phonenumber = snapshot.child("phone_number").getValue().toString();
                        String aadhar = snapshot.child("aadhar_number").getValue().toString();
                        String occup = snapshot.child("occupation").getValue().toString();
                        String age1 = snapshot.child("age").getValue().toString();
                        String gendr = snapshot.child("gender").getValue().toString();
                        String local = snapshot.child("locality").getValue().toString();
                        String vill = snapshot.child("village").getValue().toString();
                        String dist = snapshot.child("district").getValue().toString();
                        String stat = snapshot.child("state").getValue().toString();
                        String pin = snapshot.child("pincode").getValue().toString();
                        String maild = snapshot.child("email").getValue().toString();
                        fullname.setText(fullname1);
                        phone_number.setText( phonenumber);
                        aadhar_number.setText( aadhar);
                        age.setText( occup);
                        gender.setText( age1);
                        occupation.setText( gendr);
                        h_no.setText( local);
                        village.setText(vill);
                        district.setText( dist);
                        state.setText(stat);
                        pincode.setText(pin);
                        email.setText(maild);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfileView.this, "Error loading Data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initializeUI()
    {
        fullname = findViewById(R.id.tv1);
        phone_number = findViewById(R.id.tv2);
        aadhar_number = findViewById(R.id.tv3);
        age = findViewById(R.id.tv4);
        gender = findViewById(R.id.tv5);
        occupation = findViewById(R.id.tv6);
        h_no = findViewById(R.id.tv7);
        village = findViewById(R.id.tv8);
        district= findViewById(R.id.tv9);
        state = findViewById(R.id.tv10);
        pincode = findViewById(R.id.tv11);
        editProf = findViewById(R.id.editProf);
        email  = findViewById(R.id.emaild);

    }
}