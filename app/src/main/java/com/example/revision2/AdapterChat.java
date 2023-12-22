package com.example.revision2;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {
    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;
    Context context;
    List<ModelChat> chatList;
    String imageUrl;
    FirebaseUser fUser;
    private StorageReference mStorageRef;

    public AdapterChat(Context context, List<ModelChat> chatList, String imageurl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageurl;
    }

    public AdapterChat() {
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layouts: row_chat_left.xml for receiver, row_Chat_right.xml for sender
        if (i==MSG_TYPE_RIGHT) {
            View view = LayoutInflater. from(context) .inflate(R.layout.row_chat_right, viewGroup,  false);
            return new MyHolder(view);
        }
        else {
            View view = LayoutInflater. from(context) .inflate(R.layout.row_chat_left, viewGroup, false);

            return new MyHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
//get data

//get data
        String message = chatList.get(i).getMessage();
        String timeStamp = chatList.get(i). getTimestamp();
      String UserNam=chatList.get(i).getUserName();
String image=chatList.get(i).getImage();
//convert time stamp to dd/mm/yyyy hh:mm am/pm
        Calendar cal = Calendar. getInstance(Locale.ENGLISH) ;
        cal.setTimeInMillis (Long. parseLong(timeStamp) ) ;
        String dateTime = DateFormat. format( "dd/MM/yyyy hh:mm aa", cal).toString();
if((image+"").equals("null"))
{image=imageUrl;
}
//set data
        myHolder.messageTv.setText (message) ;
        myHolder.timeTv.setText (dateTime) ;
        try {
         myHolder.UserName.setText(UserNam+"");
        }catch (Exception e){
       //     Toast.makeText(context, "user "+UserNam, Toast.LENGTH_SHORT).show();

        }
        try {
            mStorageRef = FirebaseStorage.getInstance().getReference(image);
            {
                File file=File.createTempFile("tempfile",".jpg");
                mStorageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                        myHolder.profileIv.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    //    Toast.makeText(context, "Failed to retirieve image", Toast.LENGTH_SHORT).show();
                        Picasso.get().load(R.drawable.ic_back_img).into(myHolder.profileIv);

                    }
                });
            }
        }
        catch (Exception e){

        }

//set seen/delivered status of
        if (i==chatList.size()-1) {
            if (chatList.get(i).isSeen()) {
                myHolder.isSeenTv.setText("Seen");
            } else {
                myHolder.isSeenTv.setText("Delivered");
            }
        }else{
            myHolder.isSeenTv.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(fUser.getUid()))
        {
            return MSG_TYPE_RIGHT;

        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
    class MyHolder extends RecyclerView.ViewHolder
    {
        //views
        ImageView profileIv;
        TextView messageTv,timeTv,isSeenTv,UserName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profileIv=itemView.findViewById(R.id.profileIv);
            messageTv=itemView.findViewById(R.id.messageTv);
            timeTv=itemView.findViewById(R.id.timeTv);
            isSeenTv=itemView.findViewById(R.id.isSeenTv);
           UserName=(TextView)itemView.findViewById(R.id.Usertv);
        }
    }
}
