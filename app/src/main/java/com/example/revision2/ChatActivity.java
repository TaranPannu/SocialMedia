package com.example.revision2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    //view form xml
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileIv;
    TextView nameTv,userStatusTv;
    EditText messageEt;
    ImageButton sendBtn;
    TextView TextV;
    //firebase auth
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef;
    //for checking if use has seen message or not
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;
    List<ModelChat> chatList;
    private StorageReference mStorageRef;
TextView on;
 AdapterChat adapterChat;



    String hisUid;
    String myUid;
    String hisImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        nameTv=findViewById(R.id.nameIv);



        getSupportActionBar().hide();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
      //  toolbar.setTitleTextAppearance(this, R.style.MyTitleTextApperance);
on=findViewById(R.id.online);
        recyclerView =findViewById(R.id.chat_recyclerView);
//TextV=findViewById(R.id.TextV);
        profileIv =findViewById(R.id.profileIv);
        nameTv =findViewById(R.id.nameIv);
        userStatusTv =findViewById(R.id.online);
        messageEt =findViewById(R.id.messageEt);
        sendBtn =findViewById(R.id.imgBtn);
        //layout(linear layout ) for recycler view
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        //recycler view properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //on clicking user from user list we have passed that user's UID using intent so get that uid here to get the profile
        //pic ,name,and start chat with that user
        Intent intent =getIntent();
        hisUid=intent.getStringExtra("hisUid");
        // hisUid="ZrnkOOQY2EZsIA7s7AFyT4l1nTG3";
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase =FirebaseDatabase.getInstance();
        usersDbRef=firebaseDatabase.getReference("Users");

        FirebaseUser fUser = FirebaseAuth. getInstance().getCurrentUser();
        DatabaseReference ref = firebaseDatabase.getReference( "Users");
        Query query=ref.orderByChild("uid").equalTo(hisUid);
query.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot ds:snapshot.getChildren()){
            String txt=ds.child("name").getValue()+"";
            String online=ds.child("onlineStatus").getValue()+"";
            if(online.equals("online")){
                on.setText(online);
            }else{
                Calendar cal = Calendar. getInstance(Locale.ENGLISH) ;
                cal.setTimeInMillis (Long. parseLong(online) ) ;
                String dateTime = DateFormat. format( "dd/MM/yyyy hh:mm aa", cal).toString();
                on.setText("Last seen at: "+dateTime);

            }
    //     Toast.makeText(ChatActivity.this, , Toast.LENGTH_SHORT).show();
            try {
                hisImage=ds.child("image").getValue()+"";
                nameTv.setText(txt);
                ImageView ivBasicImage = (ImageView) findViewById(R.id.profileIv);
                try {
                    mStorageRef = FirebaseStorage.getInstance().getReference(ds.child("image").getValue()+"");
                    {
                        File file=File.createTempFile("tempfile",".jpg");
                        mStorageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                                ivBasicImage.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChatActivity.this, "Failed to retirieve image", Toast.LENGTH_SHORT).show();
                                Picasso.get().load(R.drawable.ic_back_img).into(ivBasicImage);

                            }
                        });
                    }
                }
                catch (Exception e){

                }
               // Picasso.get().load(ds.child("image").getValue()+"").into(ivBasicImage);
            }
            catch (Exception e){

            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
        //click button to send message
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=messageEt.getText().toString().trim();
                //check if text is empty or not
                if(TextUtils.isEmpty(message)){
                    //text empty
                 Toast.makeText(ChatActivity.this, "Cannot send empty message", Toast.LENGTH_SHORT).show();
                }else{

                    sendMessage(message);
                }

            }
        });
        seenMessage();
        readMessages();
    }

    private void seenMessage() {
        userRefForSeen=FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren())
                {
                 ModelChat chat=ds.getValue(ModelChat.class);
               //     Toast.makeText(ChatActivity.this, chat.getReceiver()+" here "+chat.getSender(), Toast.LENGTH_SHORT).show();
                    try{
             if(chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)){
                    HashMap<String, Object> hasSeenHashMap=new HashMap<>();
                       hasSeenHashMap.put("isSeen",true);
                       ds.getRef().updateChildren(hasSeenHashMap);

                    }}
                 catch (Exception e){
                    // Toast.makeText(ChatActivity.this, e+"", Toast.LENGTH_SHORT).show();
                 }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(ChatActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readMessages() {
        chatList =new ArrayList<>();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot ds: snapshot.getChildren() ){
                    ModelChat chat=ds.getValue(ModelChat.class);
                    try{
                    if(chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)||
                            chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)){
                        chatList.add(chat);
                    }}
                    catch (Exception e){
                   //     Toast.makeText(ChatActivity.this, e+"", Toast.LENGTH_SHORT).show();

                    }
                    //adapter
                    adapterChat=new AdapterChat(ChatActivity.this,chatList,hisImage);
                    adapterChat.notifyDataSetChanged();
                    //set Adapter to recycler view
                    recyclerView.setAdapter(adapterChat);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "cancelled", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void sendMessage(String message) {

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        String timestamp=String.valueOf(System.currentTimeMillis());


        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("sender",myUid);
        hashMap.put("receiver",hisUid);
        hashMap.put("message",message);
        hashMap.put("timestamp",timestamp);
        hashMap.put("isSeen",false);
        hashMap.put("UserName","");

        hashMap.put("message",messageEt.getText().toString());
        databaseReference.child("Chats").push().setValue(hashMap);
        //reset editText afetr sending message
        messageEt.setText("");
    }
void checkOnlineStatus(String status){
        DatabaseReference dberf=FirebaseDatabase.getInstance().getReference("Users").child(myUid);
    HashMap<String,Object> v=new HashMap<>();
        v.put("onlineStatus",status);

        // dberf.updateChildren(v);


}
    private void checkUserStatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!= null){
            myUid=user.getUid();// currently signed in user uid

        }
        else
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();

            super.onStart();
   checkOnlineStatus("online");

        //  Toast.makeText(ChatActivity.this,"Started",Toast.LENGTH_SHORT).toString();
    }

   /* @Override
    protected void onPause() {
        String timestamp=String.valueOf(System.currentTimeMillis());

        checkOnlineStatus(timestamp);
        userRefForSeen.removeEventListener(seenListener);

        super.onPause();
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

  /*  @Override
    protected void onResume() {
       checkOnlineStatus("online");

        super.onResume();
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

}