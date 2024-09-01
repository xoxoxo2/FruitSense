package com.example.fruitsense5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registration_page extends AppCompatActivity {

    public static final String TAG = "TAG";
    TextView haveAccountTextView;
    EditText regEmail, regPassword, regConfirmpass, regUsername, regPhone;
    Button registBtn;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        regUsername = findViewById(R.id.inputusername);
        regEmail = findViewById(R.id.inputemail);
        regPhone = findViewById(R.id.inputnumber);
        regPassword = findViewById(R.id.inputpassword);
        regConfirmpass = findViewById(R.id.inputpassword2);
        registBtn = findViewById(R.id.registerbtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        haveAccountTextView = findViewById(R.id.haveaccount);
        haveAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration_page.this, MainActivity.class);
                startActivity(intent);
            }
        });

        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = regEmail.getText().toString().trim();
                String password = regPassword.getText().toString().trim(); // Added password field
                String password2 = regConfirmpass.getText().toString().trim();
                String name = regUsername.getText().toString().trim();
                String phone = regPhone.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    regEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    regPassword.setError("Password is required");
                    return;
                }

                if (!password.equals(password2)) {
                    regConfirmpass.setError("Passwords do not match");
                    return;
                }

                if (!email.matches(emailPattern)) {
                    regEmail.setError("Enter valid email address");
                    return;
                }

                if (password.length() < 6) {
                    regPassword.setError("Password must be at least 6 characters");
                    return;
                }

                // Register user in Firebase
                fAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(registration_page.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Firebase authentication successful, now add user data to Firestore
                                    FirebaseUser user = fAuth.getCurrentUser();
                                    if (user != null) {
                                        userID = user.getUid();
                                        DocumentReference documentReference = fStore.collection("users").document(userID);
                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("fname", name);
                                        userData.put("femail", email);
                                        userData.put("fphone", phone);
                                        userData.put("fpassword", password); // Store password in Firestore
                                        documentReference.set(userData)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d(TAG, "User profile is created for " + userID);
                                                        Toast.makeText(registration_page.this, "User Created", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e(TAG, "Error adding user document", e);
                                                        Toast.makeText(registration_page.this, "Failed to register user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Log.e(TAG, "Current user is null");
                                        Toast.makeText(registration_page.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(registration_page.this, "Failed to register user: " + task.getException(), Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Error creating user", task.getException());
                                }
                            }
                        });
            }
        });
    }
}
