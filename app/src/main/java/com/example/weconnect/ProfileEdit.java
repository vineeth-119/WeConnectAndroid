package com.example.weconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileEdit extends AppCompatActivity implements DataAdd {
    TextInputEditText fullname,phone_number,aadhar_number,age,gender,occupation,h_no, village, district,state,pincode ;
    Button submit;
    DatabaseReference dref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        initializeUI();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addData();
                        Intent inta= new Intent(ProfileEdit.this,HomeActivity.class);
                        startActivity(inta);
                    }
                });

    }

      public void addData() {
        String name = fullname.getText().toString().trim();
        String adhar = aadhar_number.getText().toString().trim();
        String phoneno = phone_number.getText().toString().trim();
        String age1 = age.getText().toString().trim();
        String gend = gender.getText().toString().trim();
        String occup = occupation.getText().toString().trim();
        String hno = h_no.getText().toString().trim();
        String vill = village.getText().toString().trim();
        String dist = district.getText().toString().trim();
        String stat = state.getText().toString().trim();
        String pin = pincode.getText().toString().trim();
        Profile profile = new Profile();
        profile.setFull_name(name);
        profile.setAadhar_number(adhar);
        profile.setPhone_number(phoneno);
        profile.setAge(age1);
        profile.setGender(gend);
        profile.setOccupation(occup);
        profile.setLocality(hno);
        profile.setVillage(vill);
        profile.setDistrict(dist);
        profile.setState(stat);
        profile.setPincode(pin);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        profile.setEmail(user.getEmail());
        dref = FirebaseDatabase.getInstance("https://weconnect-b5543-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Profile").child(user.getUid());
        dref.setValue(profile);
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(String.valueOf(fullname)).build();
            user.updateProfile(profileUpdates);
        }
        Toast.makeText(ProfileEdit.this, "User Data Uploaded", Toast.LENGTH_SHORT).show();
    }

    public void initializeUI()
    {
        fullname = findViewById(R.id.name);
        phone_number = findViewById(R.id.phno);
        aadhar_number = findViewById(R.id.aadhar);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gend);
        occupation = findViewById(R.id.occ);
        h_no = findViewById(R.id.loc);
        village = findViewById(R.id.vil);
        district= findViewById(R.id.dist);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pin);
        submit = findViewById(R.id.button1);
    }


}