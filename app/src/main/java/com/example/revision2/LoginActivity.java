package com.example.revision2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText emailET;
    EditText password;
    Button LoginBtn;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    ActionBar actionBar;
    TextView notHaveAccntTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailET=findViewById(R.id.email);
        password=findViewById(R.id.password);
        LoginBtn=findViewById(R.id.loginBtn);
        notHaveAccntTv=findViewById(R.id.nothave_accountTv);
        mAuth = FirebaseAuth.getInstance();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0f0f0e"));
        // Set BackgroundDrawable
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(colorDrawable);

        progressDialog = new ProgressDialog(this);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email1 = emailET.getText ().toString();
                String password1 = password.getText().toString() .trim();
                if (!Patterns.EMAIL_ADDRESS.matcher (email1) .matches()) {
                    emailET.setError("Invalid Email");
                    emailET.setFocusable (true) ;

                }
                else {
                 LoginUser(email1,password1);
                }
            }
        });

        notHaveAccntTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
    private void LoginUser(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));

                    finish();
                } else {
                    progressDialog.dismiss();

                    Toast.makeText(LoginActivity.this, "Login Failure", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}