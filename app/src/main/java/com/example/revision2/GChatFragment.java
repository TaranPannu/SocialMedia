package com.example.revision2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
String myUid;
EditText messageEt;
ImageButton sendBtn;
RecyclerView recyclerView;
List<ModelChat> chatList;
AdapterChat adapterChat;
    FirebaseUser user;
   public String myImage;
   public String UserName;
    public GChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GChatFragment newInstance(String param1, String param2) {
        GChatFragment fragment = new GChatFragment();
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
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        //layout(linear layout ) for recycler view
        recyclerView =view.findViewById(R.id.chat_recyclerView);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        //recycler view properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
// ...

        firebaseAuth=FirebaseAuth.getInstance();
         user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
            myUid = user.getUid();

        mDatabase.child("Users").child(myUid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                myImage= task.getResult().child("image").getValue()+"";
                    UserName=task.getResult().child("name").getValue()+"";
                }
            }
        });
            messageEt = view.findViewById(R.id.messageEt);
            sendBtn = view.findViewById(R.id.imgBtn);
sendBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        sendMessage(messageEt.getText().toString());

    }
});


readMessages();
return view;
    }
    private void readMessages() {
        chatList =new ArrayList<>();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Group_Chat");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot ds: snapshot.getChildren() ){
                    ModelChat chat=ds.getValue(ModelChat.class);
                    try{
                            chatList.add(chat);
                }
                    catch (Exception e){
                    }
                    //adapter
                    adapterChat=new AdapterChat(getActivity(),chatList,"");
                    adapterChat.notifyDataSetChanged();
                    //set Adapter to recycler view
                    recyclerView.setAdapter(adapterChat);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {

        try{

            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        String timestamp=String.valueOf(System.currentTimeMillis());


        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("sender",myUid);
        hashMap.put("timestamp",timestamp);
        hashMap.put("message",message);
        hashMap.put("image", myImage+"");
        hashMap.put("UserName", UserName+"");

    databaseReference.child("Group_Chat").push().setValue(hashMap);

        messageEt.setText("");
}catch (Exception e){
     Toast.makeText(getActivity(), e+"", Toast.LENGTH_SHORT).show();

}
    }


}