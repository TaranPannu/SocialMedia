package com.example.revision2;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mDatabase;
    private Button btnUpload;
    private EditText Des,Title;
ImageView ImgBtn;
    private Uri mImageUri;
    StorageReference fileReference;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
FirebaseAuth mauth;
    private static final int PICK_IMAGE_REQUEST =1 ;
    String uid;

    public UploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();
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
        View view= inflater.inflate(R.layout.fragment_upload, container, false);
        btnUpload=view.findViewById(R.id.btnUpload);
        Des=view.findViewById(R.id.DesUpload);
        Title=view.findViewById(R.id.TitleUpload);
        ImgBtn=view.findViewById(R.id.ImageUpload);
        mauth= FirebaseAuth.getInstance();
        FirebaseUser currentUser = mauth.getCurrentUser();
       uid= currentUser.getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference("posts");
        ImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();

                upload(Des.getText()+"",Title.getText()+"");
Des.setText("");
Title.setText("");
            }
        });

        return view;
    }

    private void upload(String Des,String title) {
        mDatabase = FirebaseDatabase.getInstance().getReference("Post");
        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put ("description", Des);
        hashMap.put ("image", (fileReference+"").substring(32));
        hashMap.put ("title", title);

        //ModelPost user = new ModelPost(Des, title,(fileReference+"").substring(32,(fileReference+"").length()));
        mDatabase.push().setValue(hashMap);
        Toast.makeText(getActivity(),"Uploaded",Toast.LENGTH_SHORT).show();

    }
    private void openFileChooser() {
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
            try {
                Picasso.get().load(String.valueOf(mImageUri+"")).into(ImgBtn);//add dependency

            }
            catch(Exception e){

            }
        }
    }

    private void uploadFile() {
        if (mImageUri != null) {
            fileReference = mStorageRef.child(System.currentTimeMillis()
                    + ".post");//here we are creating a unique name to store file in a firebase storage "1672017241061.jpg" later on we will
            //use it in realtime database to refer to particular storage in firebase
            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                   // Toast.makeText(RegisterActivity.this, ""+e, Toast.LENGTH_SHORT).show();

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