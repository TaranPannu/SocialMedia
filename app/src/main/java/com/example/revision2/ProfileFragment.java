package com.example.revision2;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST =1 ;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView avatarIv;
    TextView nameTv,emailTv,phoneTv;
    private StorageReference mStorageRef;
    private StorageReference mStorageRef1;

    TextView Updatename,UpdateEmail;
Button UpdateProfile;
    StorageReference fileReference;
    private StorageTask mUploadTask;

Button Imgpick;
    private Uri mImageUri;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        mStorageRef1=FirebaseStorage.getInstance().getReference("uploads/");
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        Imgpick=view.findViewById(R.id.imgUpdate);
        Imgpick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });
Updatename=view.findViewById(R.id.nameUpdate);
UpdateEmail=view.findViewById(R.id.emailUpdate);
        UpdateProfile=view.findViewById(R.id.UpdateProfile);
        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (!Patterns.EMAIL_ADDRESS.matcher (UpdateEmail.getText().toString()) .matches()) {
                        UpdateEmail.setError("Invalid Email");
                        UpdateEmail.setFocusable (true) ;
                    }else
             uploadinfo();


            }
        });
//init view
        avatarIv =view.findViewById(R.id.avatarIv) ;
        nameTv =view. findViewById(R.id.nameTv) ;
        emailTv =view. findViewById(R.id.emailTv) ;
        phoneTv =view. findViewById(R.id.phoneTv) ;

/*We have to get info of currently signed in user. We can get it using user's email or uid
I'm gonna retrieve user detail using email*/
/*By using orderByChild, query we will Show the detail from a node
whose key  value equal to signed in email..it will search all nodes ,,where key matches it will
give detail
*/
       // Query query = databaseReference.orderByChild ("email") .equalTo(user.getEmail());
        databaseReference.addValueEventListener (new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//checke until required data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//get data
                    if(!ds.child("uid").getValue().equals(user.getUid()))
                        continue;
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();

                    //set data
                    nameTv.setText(name);
                    emailTv.setText(email);
                    phoneTv.setText(phone);

                   try {
                        mStorageRef = FirebaseStorage.getInstance().getReference(image);
                         {
                            File file=File.createTempFile("tempfile",".jpg");
                            mStorageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                                   //  Picasso.get().load(bitmap+"").fit().centerCrop().into(avatarIv);
                           avatarIv.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                  Toast.makeText(getActivity(), "Failed to retirieve"+e, Toast.LENGTH_SHORT).show();
                                    UpdateEmail.setText(e+"");
                                    Picasso.get().load(R.drawable.ic_face_foreground).into(avatarIv);

                                }
                            });
                        }
//Picasso.get().load(image).into(avatarIv);

                    }
                    catch(Exception e)
                    {
                        //if there is a exception
                        Picasso.get().load(R.drawable.ic_face_foreground).into(avatarIv);

                    }

                }}
            public void onCancelled(DatabaseError databaseError){

            }
        });
        return view;
    }

    private void uploadinfo() {
        uploadFile();

        user.updateEmail(UpdateEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child(user.getUid()).child("image").setValue((fileReference+"").substring(32));

                            databaseReference.child(user.getUid()).child("name").setValue(Updatename.getText().toString());
                            databaseReference.child(user.getUid()).child("email").setValue(user.getEmail());
//Updating email where in database ....
                            Toast.makeText(getActivity(), "Updated "+user.getEmail(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "not Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private    void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
        }
    }


    private void uploadFile() {
        if (mImageUri != null) {
            fileReference = mStorageRef1.child(System.currentTimeMillis()
                    + "." +"jpg");//here we are creating a unique name to store file in a firebase storage "1672017241061.jpg" later on we will
            //use it in realtime database to refer to particular storage in firebase
            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                }
            });

        }else{
        }
    }

}