package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatMessage extends AppCompatActivity {

    private ImageView iv_NoMessage;
    private TextView tv_NoMessage;
    private ImageButton BChatBackToPrevious;
    private RecyclerView recyclerViewChat;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private ArrayList<Model_ChatList> ChatList;
    private ArrayList<Model_User> UserList;

    private AdapterChat adapterChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        tv_NoMessage = findViewById(R.id.tv_NoMessage);
        iv_NoMessage = findViewById(R.id.iv_NoMessage);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        BChatBackToPrevious = findViewById(R.id.BChatBackToPrevious);

        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        loadChat();

        BottomNavigationView buyerNV = findViewById(R.id.buyerNavigation);
        //set Home Selected
        buyerNV.setSelectedItemId(R.id.buyerMessage);
        buyerNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.buyerHome:
                        startActivity(new Intent(getApplicationContext(), BuyerHome.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.buyerOrder:
                        startActivity(new Intent(getApplicationContext(), Orders.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.buyerMessage:
                        return true;

                    case R.id.buyerAccount:
                        startActivity(new Intent(getApplicationContext(), BuyerAccount.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        BChatBackToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadChat() {

        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ChatList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(currentUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatList.clear();
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Model_ChatList model_chatList = ds.getValue(Model_ChatList.class);
                        ChatList.add(model_chatList);

                    }
                    readChatList();
                }else {
                    tv_NoMessage.setVisibility(View.VISIBLE);
                    iv_NoMessage.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void readChatList() {
        UserList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_User model_user = ds.getValue(Model_User.class);
                    for (Model_ChatList model_chatList: ChatList){
                        if (model_user.getUID() != null && model_user.getUID().equals(model_chatList.getReceiverID()) ){
                            UserList.add(model_user);
                            break;
                        }
                    }

                    //setup adapter
                    adapterChat = new AdapterChat(ChatMessage.this, UserList);
                    adapterChat.notifyDataSetChanged();
                    //set adapter
                    recyclerViewChat.setAdapter(adapterChat);
                    //set last Message
                    for (int i=0; i<UserList.size(); i++){
                        lastMessage(UserList.get(i).getUID());

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void lastMessage(final String receiverID) {
        DatabaseReference LastMsgReference = FirebaseDatabase.getInstance().getReference("Chats");
        LastMsgReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String theLastMessage = "default";
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Chat model_chat = ds.getValue(Model_Chat.class);

                    if (model_chat==null){
                        continue;
                    }

                    String sender = model_chat.getSenderID();
                    String receiver = model_chat.getReceiverID();

                    if (sender == null || receiver == null){
                        continue;
                    }

                    if (model_chat.getReceiverID().equals(currentUser.getUid()) && model_chat.getSenderID().equals(receiverID) || model_chat.getReceiverID().equals(receiverID) && model_chat.getSenderID().equals(currentUser.getUid())){
                        if (model_chat.getMessageType().equals("Image")){
                            theLastMessage = "Sent a photo";
                        }
                        else{
                            theLastMessage = model_chat.getMessage();
                        }
                    }
                }
                adapterChat.setLastMessageMap(receiverID, theLastMessage);
                adapterChat.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}