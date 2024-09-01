package com.example.fruitsense5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class camera_page extends AppCompatActivity {

    ImageView cameraImg, imageViewGallery;
    int imageSize = 224;

    //Main Content
    ImageView tipsImg;
    TextView tipsTxt;

    //Menu Navigation
    ImageView home, camera, profile;
    TextView homeTxt, cameraTxt, profileTxt;

    private static final String TAG = "camera_page";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_page);

        //Main Content
        tipsImg = findViewById(R.id.snapTipsImage);
        tipsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(camera_page.this, tips_page.class);
                startActivity(intent);
            }
        });

        tipsTxt = findViewById(R.id.snapTipsText);
        tipsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(camera_page.this, tips_page.class);
                startActivity(intent);
            }
        });

        //menu navigation
        home = findViewById(R.id.homeImage);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(camera_page.this, home_page.class);
                startActivity(intent);
            }
        });

        homeTxt = findViewById(R.id.homeText);
        homeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(camera_page.this, home_page.class);
                startActivity(intent);
            }
        });

        camera = findViewById(R.id.cameraImage);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(camera_page.this, camera_page.class);
                startActivity(intent);
            }
        });

        cameraTxt = findViewById(R.id.cameraText);
        cameraTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(camera_page.this, camera_page.class);
                startActivity(intent);
            }
        });

        profile = findViewById(R.id.profileImage);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(camera_page.this, profile_page.class);
                startActivity(intent);
            }
        });

        profileTxt = findViewById(R.id.profileText);
        profileTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(camera_page.this, profile_page.class);
                startActivity(intent);
            }
        });

        cameraImg = findViewById(R.id.imageView2);
        cameraImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        imageViewGallery = findViewById(R.id.imageView4);
        imageViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openCamera() {
        if (ActivityCompat.checkSelfPermission(camera_page.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1);
        } else {
            ActivityCompat.requestPermissions(camera_page.this, new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) { // Camera
                if (data != null && data.getExtras() != null) {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    // You may want to process and display the image here
                    Log.d(TAG, "Image received from camera");
                    uploadImageToFirebase(imageBitmap);
                }
            } else if (requestCode == 100) { // Gallery
                if (data != null && data.getData() != null) {
                    Uri imageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        Log.d(TAG, "Image URI received from gallery: " + imageUri.toString());
                        uploadImageToFirebase(bitmap);
                    } catch (IOException e) {
                        Log.e(TAG, "Error getting bitmap from URI", e);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void uploadImageToFirebase(Bitmap imageBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Generating a random filename
        String randomName = UUID.randomUUID().toString();

        // Create a reference to "images" folder in Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + randomName + ".jpg");

        // Upload image data to Firebase Storage
        storageRef.putBytes(imageData)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    Toast.makeText(camera_page.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                    // Retrieve the download URL of the uploaded image
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Pass the download URL to the result page
                        Log.d(TAG, "Image uploaded to Firebase. Download URL: " + uri.toString());
                        Intent intent = new Intent(camera_page.this, result_page.class);
                        intent.putExtra("imageUri", uri.toString());
                        startActivity(intent);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful uploads
                    Toast.makeText(camera_page.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
