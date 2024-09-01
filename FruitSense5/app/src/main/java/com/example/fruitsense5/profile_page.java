package com.example.fruitsense5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class profile_page extends AppCompatActivity {

    //menu navigation
    ImageView homeNav, cameraNav, profileNav;
    TextView homeTxt, cameraTxt, profileTxt;

    //main content
    ImageView accSettingImg, languageImg;
    TextView accSettingTxt, languageTxt, logout;

    private TextView headname;
    private ImageView profileImage;
    private ConstraintLayout home, profile, scan;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        headname = findViewById(R.id.headname);
        profileImage = findViewById(R.id.profileimg);
        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        scan = findViewById(R.id.scan);
        logout = findViewById(R.id.button_signout);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userID = fAuth.getCurrentUser().getUid();

        // Initialize profile image and listen for changes
        initializeProfileImage();
        listenForUserChanges();

        // Setup click listeners for navigation
        setOnClickListeners();

        // Setup main content click listeners
        setupMainContent();

        // Inside ProfilePageActivity.java where you handle the language TextView click
        // Inside onCreate method of profile_page.java
        languageImg = findViewById(R.id.imageView3);
        languageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile_page.this, changelanguage_page.class);
                startActivityForResult(intent, 1);
            }
        });

        languageTxt = findViewById(R.id.language);
        languageTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile_page.this, changelanguage_page.class);
                startActivityForResult(intent, 1);
            }
        });

        // Load saved language preference
        //loadLocale();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
            }
        });
    }

    private void initializeProfileImage() {
        StorageReference profileRef = storageReference.child("users/" + userID + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });
    }

    private void listenForUserChanges() {
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(profile_page.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value != null && value.exists()) {
                    headname.setText(value.getString("fname"));
                }
            }
        });
    }

    private void setOnClickListeners() {
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), home_page.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Already on profile page
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), camera_page.class));
            }
        });
    }

    private void setupMainContent() {
        accSettingImg = findViewById(R.id.imageView1);
        accSettingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile_page.this, accountsetting_page.class));
            }
        });

        accSettingTxt = findViewById(R.id.textView2);
        accSettingTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile_page.this, accountsetting_page.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String selectedLanguage = data.getStringExtra("selectedLanguage");
            // Handle language change here if needed
            // For example, you can update the UI language or store language preference
            Toast.makeText(this, "Selected Language: " + selectedLanguage, Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToDb(Uri imageUri) {
        StorageReference fileRef = storageReference.child("users/" + userID + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).resize(1000, 1000).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile_page.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadLocale() {
        SharedPreferences preferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String languageCode = preferences.getString("language", "");

        if (!languageCode.isEmpty()) {
            Locale locale = new Locale(languageCode);
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.setLocale(locale);
            getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}
