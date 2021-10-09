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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Complaint extends AppCompatActivity implements DataAdd{
    TextInputEditText fullname,phone_number,aadhar_number,age,subject,complaint,locality, village, district,state,pincode ;
    Button submit;
    DatabaseReference dref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        initializeUI();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
               // Toast.makeText(Complaint.this, "Upload Finished", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Complaint.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
    public void initializeUI()
    {
        fullname = findViewById(R.id.name1);
        phone_number = findViewById(R.id.phno1);
        aadhar_number = findViewById(R.id.aadhar1);
        age = findViewById(R.id.age1);
        subject = findViewById(R.id.sub);
        complaint = findViewById(R.id.comp);
        locality = findViewById(R.id.loc1);
        village = findViewById(R.id.vil1);
        district= findViewById(R.id.dist1);
        state = findViewById(R.id.state1);
        pincode = findViewById(R.id.pin1);
        submit = findViewById(R.id.buttonsub);
       // Toast.makeText(Complaint.this, "Data Initialized", Toast.LENGTH_LONG).show();
    }

    public void addData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = fullname.getText().toString().trim();
        String adhar = aadhar_number.getText().toString().trim();
        String phoneno = phone_number.getText().toString().trim();
        String aged = age.getText().toString().trim();
        String subj = subject.getText().toString().trim();
        String comp = complaint.getText().toString().trim();
        String loc = locality.getText().toString().trim();
        String vill = village.getText().toString().trim();
        String dist = district.getText().toString().trim();
        String stat = state.getText().toString().trim();
        String pin = pincode.getText().toString().trim();

        ComplaintDetails complaintDetails = new ComplaintDetails();
        complaintDetails.setName(name);
        complaintDetails.setAadharNumber(adhar);
        complaintDetails.setPhoneNumber(phoneno);
        complaintDetails.setAge(aged);
        complaintDetails.setSubject(subj);
        complaintDetails.setComplaint(comp);
        complaintDetails.setLocation(loc);
        complaintDetails.setVillage(vill);
        complaintDetails.setDistrict(dist);
        complaintDetails.setState(stat);
        complaintDetails.setPincode(pin);
        complaintDetails.setEmail(user.getEmail());
        try {

            String email = user.getEmail();
            dref = FirebaseDatabase.getInstance("https://weconnect-b5543-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Complaints").child(user.getUid());
            dref.setValue(complaintDetails);
            Toast.makeText(Complaint.this, "Complaint sent,you will get updated soon..", Toast.LENGTH_LONG).show();
        }catch (Exception e)
        {
            Toast.makeText(Complaint.this, "Error "+e.getMessage(), Toast.LENGTH_LONG).show();


        }

    }
}