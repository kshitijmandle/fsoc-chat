package com.fsociety.fsocietyin.Activies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.fsociety.fsocietyin.R;
import com.fsociety.fsocietyin.adapters.ChatAdapter;
import com.fsociety.fsocietyin.models.msg_model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Base64;

public class ChatActivity extends AppCompatActivity {

    EditText msgedittext;
    ImageView sendimgbtn;
    RecyclerView chat_recycler;
    ChatAdapter chatAdapter;
    ArrayList<msg_model> messagelist;
    String sender_room;
    String reciver_room;
    FirebaseDatabase database;
    DatabaseReference myref;
    TextView action_barname;
    LinearLayoutManager LLM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messagelist = new ArrayList<>();
        String senderid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = getIntent();
        String username = intent.getStringExtra("name");
        String receiver_uid = intent.getStringExtra("userid");
        msgedittext = findViewById(R.id.messageTextBox);
        sendimgbtn = findViewById(R.id.sendimgbtn);
        chat_recycler = findViewById(R.id.chatrecyclerview);
        action_barname = findViewById(R.id.action_bar_chat);
        action_barname.setText(username);
        sender_room = receiver_uid + senderid;
        reciver_room = senderid + receiver_uid;
        //logic for adding data to recycler adapter
        database = FirebaseDatabase.getInstance();
        myref = database.getReference();
        myref.child("chats").child(sender_room).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagelist.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    msg_model temp = ds.getValue(msg_model.class);
                    messagelist.add(temp);
                    //Toast.makeText(ChatActivity.this, temp.getMsg() + " " +, Toast.LENGTH_SHORT).show();
                    Log.d("fuck", "onDataChange: " + temp.getMsg());
                }


                chatAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



       chatAdapter = new ChatAdapter(messagelist, getApplicationContext());
      //here fucked me up ffffffffffffffffffffffffuuuuuuuuuuuuuuuuuuuuccccccccccccccccckkkkkkkkkkk

       chat_recycler.setAdapter(chatAdapter);
       LLM = new LinearLayoutManager(getApplicationContext());
       LLM.setStackFromEnd(true);
       chat_recycler.setLayoutManager(LLM);







       




        sendimgbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String msg = msgedittext.getText().toString();
                String enc_msg = msg_encrypt(msg);
                msg_model msg_object = new msg_model(enc_msg,senderid);
                database = FirebaseDatabase.getInstance();
                myref = database.getReference();
                myref.child("chats").child(sender_room).child("messages").push().setValue(msg_object)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                myref.child("chats").child(reciver_room).child("messages").push().setValue(msg_object);
                            }
                        });
                msgedittext.setText("");
                chat_recycler.scrollToPosition(chatAdapter.getItemCount());
                chat_recycler.smoothScrollToPosition(chat_recycler.getAdapter().getItemCount());

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String msg_encrypt(String msg){

        String BasicBase64format
                = Base64.getEncoder()
                .encodeToString(msg.getBytes());
        return BasicBase64format;
    }
}