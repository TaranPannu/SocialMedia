package com.example.revision2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<example_item> mExampleList;
    private OnItemClickListener mListener;
    private Context mContext;
    private StorageReference mStorageRef;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public ExampleAdapter(     Context mContext,ArrayList<example_item> mExampleList) {
        this.mExampleList = mExampleList;
         this.mContext=mContext;

    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        return new ExampleViewHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        example_item currentItem = mExampleList.get(position);
        holder.mTextView1.setText(currentItem.getTv());
        try {
            mStorageRef = FirebaseStorage.getInstance().getReference(currentItem.getImage());
            {
                File file=File.createTempFile("tempfile",".jpg");
                mStorageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                        holder.image.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //  Toast.makeText(mContext, "Failed to retirieve image", Toast.LENGTH_SHORT).show();
                        Picasso.get().load(R.drawable.ic_face_foreground).into(holder.image);

                    }
                });
            }
        }
        catch (Exception e){

        }        holder.itemView.setOnClickListener((v)-> {
         //   Toast.makeText(mContext, "hello"+currentItem.getUid(), Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(mContext, ChatActivity.class);
            intent.putExtra("hisUid",currentItem.getUid());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView1;
ImageView image;
        public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.Tv);
            image=itemView.findViewById(R.id.Iv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
