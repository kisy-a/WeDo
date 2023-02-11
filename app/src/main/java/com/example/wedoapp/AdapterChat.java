package com.example.wedoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.HolderChat> {

    private Context context;
    private ArrayList<Model_User> UserList;
    private HashMap<String, String> lastMessageMap;

    FirebaseUser user;

    public AdapterChat(Context context, ArrayList<Model_User> userList) {
        this.context = context;
        this.UserList = userList;
        lastMessageMap = new HashMap<>();
    }

    @NonNull
    @Override
    public AdapterChat.HolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row inbox list.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_list, parent, false);
        return new AdapterChat.HolderChat(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChat.HolderChat holder, int position) {
        Model_User model_user = UserList.get(position);

        //get data
        //String LastMessage = model_user.getMessage();
        String ReceiverID = UserList.get(position).getUID();
        String UserName = UserList.get(position).getFullName();
        String UserImage = UserList.get(position).getProfileImage();
        String lastMessage = lastMessageMap.get(ReceiverID);

        //convert timestamp to proper format dd/MM/yyyy
//        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
//        calendar.setTimeInMillis(Long.parseLong(MID));
//        String dateFormat = DateFormat.format("dd-MM-yyyy hh:mm aa", calendar).toString();

        //set data
        //holder.chat_userMessage.setText(LastMessage);
        holder.chat_userName.setText(UserName);

        if (lastMessage == null || lastMessage.equals("default")){
            holder.chat_userMessage.setVisibility(View.GONE);
        }
        else {
            holder.chat_userMessage.setVisibility(View.VISIBLE);
            holder.chat_userMessage.setText(CensorWords.censor(lastMessage));
        }

        try {
            Picasso.get().load(UserImage).placeholder(R.drawable.profile).into(holder.chat_userImage);
        }
        catch (Exception e) {
            holder.chat_userImage.setImageResource(R.drawable.profile);
        }
//        holder.Time.setText(dateFormat);

        //handle click of user in chatList
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start chat activity with that user
                Intent intentToHistoryChat = new Intent(context, ChatInbox.class);
                intentToHistoryChat.putExtra("ReceiverID", ReceiverID);
                context.startActivity(intentToHistoryChat);
            }
        });

        //set holder color
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    v.setBackgroundColor(Color.parseColor("#f0f0f0"));
                }
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });

    }

    public void setLastMessageMap (String receiverID, String lastMessage){
        lastMessageMap.put(receiverID, lastMessage);
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    public class HolderChat extends RecyclerView.ViewHolder {

        TextView chat_userName, chat_userMessage;
        ImageView chat_userImage;

        public HolderChat(@NonNull View itemView) {
            super(itemView);

            chat_userImage = itemView.findViewById(R.id.chat_userImage);
            chat_userMessage = itemView.findViewById(R.id.chat_userMessage);
            chat_userName = itemView.findViewById(R.id.chat_userName);

        }
    }
}
