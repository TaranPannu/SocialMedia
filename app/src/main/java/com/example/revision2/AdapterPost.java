package com.example.revision2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.ExampleViewHolder> {


    private ArrayList<example_post> mExampleList;
    private Context mContext;
    private StorageReference mStorageRef;


    public AdapterPost(Context mContext, ArrayList<example_post> mExampleList) {
        this.mExampleList = mExampleList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_post, parent, false);
        return new AdapterPost.ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        example_post currentItem = mExampleList.get(position);
        holder.mTextView1.setText(currentItem.getTitle());
        holder.Desc.setText(currentItem.getDescription());
        holder.username.setText("Taranpreet");
        try {
            Picasso.get().load(R.mipmap.ic_city_round).into(holder.userimg);
        }
        catch (Exception e){

        }
        try {
            mStorageRef = FirebaseStorage.getInstance().getReference(currentItem.getImage());
            {
                File file = File.createTempFile("tempfile", ".jpg");
                mStorageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        holder.image.setImageBitmap(bitmap);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Picasso.get().load(R.drawable.ic_face_foreground).into(holder.image);

                    }
                });
            }
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1,Desc,username;
        ImageView image,userimg;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.TitlePost);
            image = itemView.findViewById(R.id.ImagePost);
Desc=itemView.findViewById(R.id.DesPost);
username=itemView.findViewById(R.id.Userpost);
userimg=image.findViewById(R.id.userimgpost);
        }
    }
}