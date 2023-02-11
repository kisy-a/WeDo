package com.example.wedoapp;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdapterChatConversation extends RecyclerView.Adapter<AdapterChatConversation.HolderChatConversation> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private ArrayList<Model_Chat> ChatConversationList;

    FirebaseUser user;

    public AdapterChatConversation(Context context, ArrayList<Model_Chat> chatConversationList){
        this.context = context;
        this.ChatConversationList = chatConversationList;
    }
    @NonNull
    @Override
    public AdapterChatConversation.HolderChatConversation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(context).inflate(R.layout.user_chat_right, parent, false);
            return new AdapterChatConversation.HolderChatConversation(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.user_chat_left, parent, false);
            return new AdapterChatConversation.HolderChatConversation(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChatConversation.HolderChatConversation holder, int position) {
        Model_Chat model_chat = ChatConversationList.get(position);

        //get data
        //String Message = model_chat.getMessage();

        String Message = CensorWords.censor(ChatConversationList.get(position).getMessage());
        String MID = ChatConversationList.get(position).getMID();
        String MessageType = ChatConversationList.get(position).getMessageType();

        //convert timestamp to proper format dd/MM/yyyy
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(MID));
        String dateFormat = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar).toString();

        if (MessageType.equals("Text")){

            //text message
            holder.msg.setVisibility(View.VISIBLE);
            holder.messageImage.setVisibility(View.GONE);

            holder.msg.setText(Message);

        }
        else{

            //image message
            holder.msg.setVisibility(View.GONE);
            holder.messageImage.setVisibility(View.VISIBLE);

            try {
                Picasso.get().load(Message).placeholder(R.drawable.ic_no_image).into(holder.messageImage);
            }
            catch (Exception e) {
                holder.messageImage.setImageResource(R.drawable.ic_no_image);
            }

        }

        holder.Time.setText(dateFormat);

    }

    @Override
    public int getItemCount() {
        return ChatConversationList.size();
    }

    public class HolderChatConversation extends RecyclerView.ViewHolder{

        ImageView messageImage;
        TextView msg, Time;


        public HolderChatConversation(@NonNull View itemView) {
            super(itemView);

            messageImage = itemView.findViewById(R.id.messageImage);
            Time = itemView.findViewById(R.id.Time);
            msg = itemView.findViewById(R.id.msg);

        }
    }
    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (ChatConversationList.get(position).getSenderID().equals(user.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }

}
