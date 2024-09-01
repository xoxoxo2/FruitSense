package com.example.fruitsense5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class accountsetting_page extends AppCompatActivity {

    TextView tname, temail, tphone, tpassword, headname;
    ImageView profileImage, back;
    Button buttonEdit;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String UserID;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsetting_page);

        back = findViewById(R.id.backImageView);
        buttonEdit = findViewById(R.id.button_edit);
        profileImage = findViewById(R.id.profileimg);
        headname = findViewById(R.id.headname);
        tname = findViewById(R.id.name);
        tphone = findViewById(R.id.phone);
        temail = findViewById(R.id.email);
        tpassword = findViewById(R.id.password); // Assuming you have a TextView for password

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        UserID = fAuth.getCurrentUser().getUid();

        // Load user profile image
        loadProfileImage();

        // Fetch and display user data from Firestore
        fetchUserData();

        // Handle back button click
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), profile_page.class);
                startActivity(intent);
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(accountsetting_page.this, editprofile_page.class);
                startActivity(intent);
            }
        });
    }

    private void loadProfileImage() {
        StorageReference profileRef = storageReference.child("users/" + UserID + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(accountsetting_page.this, "Failed to load profile image. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserData() {
        DocumentReference documentReference = fStore.collection("users").document(UserID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(accountsetting_page.this, "Error getting user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value != null && value.exists()) {
                    headname.setText(value.getString("fname"));
                    tname.setText(value.getString("fname"));
                    temail.setText(value.getString("femail"));
                    tphone.setText(value.getString("fphone"));

                    // Fetch and display password
                    String password = value.getString("fpassword");
                    tpassword.setText(password); // Assuming you have a TextView for password
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            uploadImageToDb(imageUri);
        }
    }

    private void uploadImageToDb(Uri imageUri) {
        StorageReference fileRef = storageReference.child("users/" + UserID + "/profile.jpg");
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
                Toast.makeText(accountsetting_page.this, "Save Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
