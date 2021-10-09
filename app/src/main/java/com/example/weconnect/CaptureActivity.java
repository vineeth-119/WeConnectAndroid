package com.example.weconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class CaptureActivity extends AppCompatActivity {

    private Button camera;
    private Button upload;
    private ImageView photo;
    private Bitmap image;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        camera = findViewById(R.id.camera);
        upload = findViewById(R.id.upload);
        photo = findViewById(R.id.image);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

         camera.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 startActivityForResult(camera, 0);
             }
         });
         upload.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                capture();
             }
         });
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            image = (Bitmap) data.getExtras().get("data");
            photo.setImageBitmap(image);

        }
    }
       private void capture() {

                final ProgressBar p = findViewById(R.id.progressbar);

                p.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = user.getEmail();
                StorageReference imageRef = mStorageRef.child("Criminal").child(email);
                byte[] b = stream.toByteArray();
                imageRef.putBytes(b)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                p.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri downloadUri = uri;
                                        
                                    }
                                });

                                Toast.makeText(CaptureActivity.this, "Photo Uploaded...check your Mail for result", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CaptureActivity.this,HomeActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                p.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(CaptureActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
        }
}

