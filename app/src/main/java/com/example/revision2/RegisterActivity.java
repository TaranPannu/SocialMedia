package com.example.revision2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST =1 ;
    EditText email;
EditText password;
EditText UserName;
String username;
Button RegisterBtn;
ImageView imageView;
FirebaseAuth mAuth;
ProgressDialog progressDialog;
TextView have_accountTv;
private Uri mImageUri;
    StorageReference fileReference;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        RegisterBtn=findViewById(R.id.registerBtn);
        mAuth = FirebaseAuth.getInstance();
        UserName=findViewById(R.id.UserName);
        imageView=findViewById(R.id.UserImg);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#0f0f0e"));
        // Set BackgroundDrawable
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("REGISTER");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });
        have_accountTv=findViewById(R.id.have_accountTv);
        have_accountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        progressDialog = new ProgressDialog(this);
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
                String em=email.getText().toString().trim();
                String pass=password.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher (em) .matches() || !RegisterActivity_Tests(em,pass)) {
                    email.setError("Invalid Email");
                    email.setFocusable (true) ;
                }else if (password.length() < 6) {
                    password.setError("Password length >= 6");
                    password.setFocusable(true);
                }else
                    registerUser(em,pass);
            }
        });
    }
    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    username=UserName.getText().toString().trim();
                    username=username.substring(0, 1).toUpperCase() + username.substring(1);//to first letter capital of each user name
                    FirebaseUser user=mAuth.getCurrentUser();
                    String email = user.getEmail();
                    String uid = user.getUid();
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put ("email", email);
                    hashMap.put ("uid", uid);
                    hashMap.put ("name", ""+username);
                    hashMap.put ("onlineStatus", "online");

                    hashMap.put ("phone", "");
                    if (mImageUri != null)
                        hashMap.put ("image",(fileReference+"").substring(32,(fileReference+"").length())+"");// basically we only need "uploads/1672017241061.jpg"
else
                        hashMap.put ("image","uploads/1672017241061.jpg"); // default image stored in firebase for users not uploading there images
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference=database.getReference("Users");
                    reference.child(uid).setValue(hashMap);
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Registered "+username, Toast.LENGTH_SHORT).show();
                 startActivity(new Intent(RegisterActivity.this,DashboardActivity.class));

                }else{
                    Toast.makeText(RegisterActivity.this, "not Successful", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error: "+e, Toast.LENGTH_SHORT).show();

            }
        });   }

    private    void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
try {
 Picasso.get().load(String.valueOf(mImageUri+"")).into(imageView);//add dependency

}
catch(Exception e){

            }
        }
    }
private String getFileExtension(Uri uri) {//To get extension of the file
    ContentResolver cR = getContentResolver();
    MimeTypeMap mime = MimeTypeMap.getSingleton();
    return mime.getExtensionFromMimeType(cR.getType(uri));
}

private void uploadFile() {
        if (mImageUri != null) {
             fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));//here we are creating a unique name to store file in a firebase storage "1672017241061.jpg" later on we will
            //use it in realtime database to refer to particular storage in firebase
            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this, ""+e, Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                }
            });

}else{
        }
}
    static Boolean RegisterActivity_Tests(String email, String pass){
        if(email.equals("") || pass.equals(""))return false;
        if(pass.length()<6)return false;

        return true;
    }
}
