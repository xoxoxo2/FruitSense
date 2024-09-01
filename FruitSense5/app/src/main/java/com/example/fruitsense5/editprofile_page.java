package com.example.fruitsense5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class editprofile_page extends AppCompatActivity {

    EditText regEmail, regPassword, regConfirmpass, regUsername, regPhone;
    Button registBtn;
    TextView haveAccountTextView;
    ImageView profileImageView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    StorageReference storageReference;

    ImageView back;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile_page);

        regUsername = findViewById(R.id.inputusername);
        regEmail = findViewById(R.id.inputemail);
        regPhone = findViewById(R.id.inputnumber);
        regPassword = findViewById(R.id.inputpassword);
        regConfirmpass = findViewById(R.id.inputpassword2);
        registBtn = findViewById(R.id.registerbtn);
        profileImageView = findViewById(R.id.profileimg);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userID = fAuth.getCurrentUser().getUid();

        // Remove inputType setting that masks password
        regPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        regConfirmpass.setInputType(android.text.InputType.TYPE_CLASS_TEXT);

        // Fetch current user data and populate EditText fields
        fetchUserData();

        // Handle save button click
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserData();
            }
        });


        // Handle profile image click to upload
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        back = findViewById(R.id.backImageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editprofile_page.this, accountsetting_page.class);
                startActivity(intent);
            }
        });
    }

    private void fetchUserData() {
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    regUsername.setText(documentSnapshot.getString("fname"));
                    regEmail.setText(documentSnapshot.getString("femail"));
                    regPhone.setText(documentSnapshot.getString("fphone"));
                    // Fetch password if available and set it
                    if (documentSnapshot.contains("fpassword")) {
                        regPassword.setText(documentSnapshot.getString("fpassword"));
                        regConfirmpass.setText(documentSnapshot.getString("fpassword"));
                    }
                    // Fetch profile image if available
                    if (documentSnapshot.contains("profileImageUrl")) {
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                        // Load profile image using Glide or Picasso library
                        Picasso.get().load(profileImageUrl).into(profileImageView);
                    }
                } else {
                    Toast.makeText(editprofile_page.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(editprofile_page.this, "Failed to fetch user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserData() {
        String email = regEmail.getText().toString().trim();
        String name = regUsername.getText().toString().trim();
        String phone = regPhone.getText().toString().trim();
        String password = regPassword.getText().toString().trim();
        String confirmPassword = regConfirmpass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            regEmail.setError("Email is required");
            return;
        }

        if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            regEmail.setError("Enter valid email address");
            return;
        }

        if (!TextUtils.isEmpty(password)) {
            if (password.length() < 6) {
                regPassword.setError("Password must be at least 6 characters");
                return;
            }

            if (!password.equals(confirmPassword)) {
                regConfirmpass.setError("Passwords do not match");
                return;
            }
        }

        // Update Firestore document with new data
        DocumentReference documentReference = fStore.collection("users").document(userID);
        Map<String, Object> userData = new HashMap<>();
        userData.put("fname", name);
        userData.put("femail", email);
        userData.put("fphone", phone);

        documentReference.update(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(editprofile_page.this, "User data updated successfully", Toast.LENGTH_SHORT).show();
                        // Update password if changed
                        if (!TextUtils.isEmpty(password)) {
                            updatePassword(password);
                        } else {
                            navigateToAccountSettings(); // Navigate directly if no password update
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editprofile_page.this, "Failed to update user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(editprofile_page.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            // Update password field in Firestore
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> passwordData = new HashMap<>();
                            passwordData.put("fpassword", newPassword);
                            documentReference.update(passwordData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(editprofile_page.this, "Password updated in Firestore successfully", Toast.LENGTH_SHORT).show();
                                            navigateToAccountSettings(); // Navigate after all updates
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(editprofile_page.this, "Failed to update password in Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editprofile_page.this, "Failed to update password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void navigateToAccountSettings() {
        Intent intent = new Intent(editprofile_page.this, accountsetting_page.class);
        startActivity(intent);
        finish(); // Finish current activity
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Upload image to Firebase Storage
            StorageReference fileRef = storageReference.child("users/" + userID + "/profile.jpg");
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(editprofile_page.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                            // Set image in ImageView
                            profileImageView.setImageURI(imageUri);

                            // Update Firestore with image URL
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DocumentReference documentReference = fStore.collection("users").document(userID);
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("profileImageUrl", uri.toString());
                                    documentReference.update(userData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(editprofile_page.this, "Profile image updated in Firestore", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(editprofile_page.this, "Failed to update profile image in Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editprofile_page.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
