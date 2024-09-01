package com.example.fruitsense5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button loginbtn;
    EditText enteremail, enterpassword;
    TextView registerbtn, registerbtn1, forgotpass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enteremail = findViewById(R.id.email);
        enterpassword = findViewById(R.id.password);
        loginbtn = findViewById(R.id.loginbtn);
        registerbtn = findViewById(R.id.donthaveacc);
        registerbtn1 = findViewById(R.id.donthaveacc1);
        forgotpass= findViewById(R.id.forgotpass);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuth();
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, registration_page.class);
                startActivity(intent);
            }
        });

        registerbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, registration_page.class);
                startActivity(intent);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.forgotpass viewDialogAdd = new forgotpass();
                viewDialogAdd.showDialog(MainActivity.this);}

        });
    }

    public void performAuth(){
        String email = enteremail.getText().toString();
        String password = enterpassword.getText().toString();

        if (!email.matches(emailPattern)) {
            enteremail.setError("Enter Correct Email");
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            enteremail.setError("Email is required");
            enteremail.requestFocus();
        } else if (password.isEmpty() || password.length() < 6) {
            enterpassword.setError("Please Enter Password");
        } else {
            progressDialog.setMessage("Please Wait While Login..");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            loginUser(email, password);
        }
    }
    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuth = FirebaseAuth.getInstance();
                    mUser = mAuth.getCurrentUser();

                    progressDialog.dismiss();
                    sendUserToNextActivity();
                    Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void sendUserToNextActivity() {
        Intent intent = new Intent(MainActivity.this, home_page.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public class forgotpass {
        public void showDialog(Context context) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.forgot_password);

            EditText request = dialog.findViewById(R.id.request);

            dialog.findViewById(R.id.requestbtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userEmail = request.getText().toString();
                    if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                        Toast.makeText(MainActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Email Sent!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "Unable to send email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });
            dialog.findViewById(R.id.donthaveacc).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), registration_page.class);
                    startActivity(intent);
                }
            });
            dialog.findViewById(R.id.donthaveacc1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), registration_page.class);
                    startActivity(intent);
                }
            });
            dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }
}