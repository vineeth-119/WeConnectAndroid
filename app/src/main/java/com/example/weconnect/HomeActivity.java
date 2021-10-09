package com.example.weconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.UserWriteRecord;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tv;
    FirebaseAuth mFirebaseAuth;
    Button ApplyNoc, addComp, capture, profile, logout,camera,upload;
    private Bitmap image;
    private StorageReference mStorageRef;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tv = (TextView) findViewById(R.id.tv2);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tv.setText("Welcome");
        initialize();
        //mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Noc:
                Toast.makeText(HomeActivity.this, "Applying for NOC", Toast.LENGTH_SHORT).show();
                NocActivity nocActivity = new NocActivity(this);
                nocActivity.getData();

                break;
            case R.id.prof:
                Toast.makeText(HomeActivity.this, "Opening Profile", Toast.LENGTH_SHORT).show();
                Intent inte = new Intent(HomeActivity.this, ProfileView.class);
                startActivity(inte);
                break;
            case R.id.comp:
                Toast.makeText(HomeActivity.this, "Opening Complaint Box", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, Complaint.class);
                startActivity(intent);
                break;
            case R.id.CheckCrim:
                Toast.makeText(HomeActivity.this, "Opening Cam Module", Toast.LENGTH_SHORT).show();
                Intent intet = new Intent(HomeActivity.this, CaptureActivity.class);
                startActivity(intet);
                break;
            case R.id.logout:
                setLogout();
                break;
            default:
                break;
        }
    }
    private void initialize()
    {
        ApplyNoc =(Button) findViewById(R.id.Noc);
        camera =(Button) findViewById(R.id.camera);
        upload = (Button)findViewById(R.id.upload);
        addComp =(Button) findViewById(R.id.comp);
        capture =(Button) findViewById(R.id.CheckCrim);
        profile = (Button)findViewById(R.id.prof);
        logout  = (Button)findViewById(R.id.logout);
        ApplyNoc.setOnClickListener(this);
        addComp.setOnClickListener(this);
        profile.setOnClickListener(this);
        logout.setOnClickListener(this);
        capture.setOnClickListener(this);
    }

    private void takePic()
    {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 0);
    }

    private  void viewProfile()
    {
        profile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(HomeActivity.this, "Opening Profile", Toast.LENGTH_SHORT).show();
            Intent inte = new Intent(HomeActivity.this, ProfileView.class);
            startActivity(inte);
        }
    });
    }


    private  void setLogout()
    {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Logging Out", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(i);
            }
        });
    }

//    private void complaint( )
//    {
//        addComp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            image = (Bitmap) data.getExtras().get("data");
            //photo.setImageBitmap(image);

        }
    }

}

class NocActivity {
   private DatabaseReference dref,dref2;

    private Context context;
    public NocActivity(Context context)
    {
        this.context = context;
    }
    public void getData() {
        Profile profile = new Profile();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dref = FirebaseDatabase.getInstance("https://weconnect-b5543-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Profile").child(user.getUid());
        if(dref==null)
        {
            Toast.makeText(context, "User data not added ",Toast.LENGTH_SHORT).show();
            return;
        }
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profile.setFull_name(snapshot.child("full_name").getValue().toString());
                profile.setPhone_number(snapshot.child("phone_number").getValue().toString());
                profile.setAadhar_number(snapshot.child("aadhar_number").getValue().toString());
                profile.setOccupation(snapshot.child("occupation").getValue().toString());
                profile.setAge(snapshot.child("age").getValue().toString());
                profile.setGender(snapshot.child("gender").getValue().toString());
                profile.setLocality(snapshot.child("locality").getValue().toString());
                profile.setVillage(snapshot.child("village").getValue().toString());
                profile.setDistrict(snapshot.child("district").getValue().toString());
                profile.setState(snapshot.child("state").getValue().toString());
                profile.setPincode(snapshot.child("pincode").getValue().toString());
                profile.setEmail(snapshot.child("email").getValue().toString());
                String adhar = profile.getAadhar_number();
                dref = FirebaseDatabase.getInstance("https://weconnect-b5543-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("NOC Applications").child(adhar);
                dref.setValue(profile);
                Toast.makeText(context, "Applied for NOC successfully..will receive a mail shortly", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Application for NOC failed..check your profile", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
