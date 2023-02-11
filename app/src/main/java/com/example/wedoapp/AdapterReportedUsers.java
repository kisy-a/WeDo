package com.example.wedoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterReportedUsers extends RecyclerView.Adapter<AdapterReportedUsers.HolderReportedUsers> {

    private Context context;
    private ArrayList<Model_ReportedUsers> ReportedUsersList;

    public AdapterReportedUsers(Context context, ArrayList<Model_ReportedUsers> reportedUsersList){
        this.context = context;
        this.ReportedUsersList = reportedUsersList;
    }
    @NonNull
    @Override
    public AdapterReportedUsers.HolderReportedUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reported_users_list,parent,false);
        return new AdapterReportedUsers.HolderReportedUsers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReportedUsers.HolderReportedUsers holder, int position) {
        //get data
        Model_ReportedUsers model_reportedUsers = ReportedUsersList.get(position);
        String userReportID = model_reportedUsers.getReportID();
        String userReportStatus = model_reportedUsers.getReportStatus();
        String userReportCategory = model_reportedUsers.getReportCategory();
        String userReportDescription = model_reportedUsers.getReportDescription();


        //to show the user info
        loadUserDetail(model_reportedUsers, holder);

        //set data
        holder.userReportID.setText(userReportID);
        holder.userReportStatus.setText(userReportStatus);
        holder.userReportCategory.setText(userReportCategory);
        holder.userReportDescription.setText(userReportDescription);

        holder.userBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                final String timestamp = "" + System.currentTimeMillis();
                String getReportID = model_reportedUsers.getReportID();
                String ReportStatus = "Banned";

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ReportedUsers");
                databaseReference.child(getReportID).child("ReportStatus").setValue(ReportStatus);
                databaseReference.child(getReportID).child("Timestamp").setValue(timestamp);

                //Intent sIntentToRate = new Intent(context, BuyerOrderCompletedList.class);
                //context.startActivity(sIntentToRate);

                Toast.makeText(context,"User Banned",Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
        holder.userSuspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                final String timestamp = "" + System.currentTimeMillis();
                String getReportID = model_reportedUsers.getReportID();
                String ReportStatus = "Suspended";

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ReportedUsers");
                databaseReference.child(getReportID).child("ReportStatus").setValue(ReportStatus);
                databaseReference.child(getReportID).child("Timestamp").setValue(timestamp);

                //Intent sIntentToRate = new Intent(context, BuyerOrderCompletedList.class);
                //context.startActivity(sIntentToRate);

                Toast.makeText(context,"User Suspended",Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
        holder.userUnban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                final String timestamp = "" + System.currentTimeMillis();
                String getReportID = model_reportedUsers.getReportID();
                String ReportStatus = "Unbanned";

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ReportedUsers");
                databaseReference.child(getReportID).child("ReportStatus").setValue(ReportStatus);
                databaseReference.child(getReportID).child("Timestamp").setValue(timestamp);

                //Intent sIntentToRate = new Intent(context, BuyerOrderCompletedList.class);
                //context.startActivity(sIntentToRate);

                Toast.makeText(context,"User Unbanned",Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });

    }

    private void loadUserDetail(Model_ReportedUsers model_reportedUsers, HolderReportedUsers holder) {
        String reportedID = model_reportedUsers.getReportedID();

        DatabaseReference reportedRef = FirebaseDatabase.getInstance().getReference("Users");
        reportedRef.orderByChild("UID").equalTo(reportedID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();
                    holder.userReportedName.setText(FullName);

                    String SellerImage = ""+ds.child("ProfileImage").getValue();
                    try {
                        Picasso.get().load(SellerImage).placeholder(R.drawable.profile).into(holder.userReportedImg);
                    }
                    catch (Exception e) {
                        holder.userReportedImg.setImageResource(R.drawable.profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String reporterID = model_reportedUsers.getReporterID();
        DatabaseReference reporterRef = FirebaseDatabase.getInstance().getReference("Users");
        reporterRef.orderByChild("UID").equalTo(reporterID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();

                    holder.userReporterName.setText(FullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void clear() {
        int size = ReportedUsersList.size();
        ReportedUsersList.clear();
        notifyItemRangeRemoved(0, size);
    }
    @Override
    public int getItemCount() {
        return ReportedUsersList.size();
    }

    public class HolderReportedUsers extends RecyclerView.ViewHolder{
        TextView userReportID,userReportStatus,userReportCategory,userReportDescription, userReportedName, userReporterName;
        ImageView userReportedImg;
        Button userBan,userSuspend,userUnban;

        public HolderReportedUsers(@NonNull View itemView) {
            super(itemView);

            userBan = itemView.findViewById(R.id.userBan);
            userSuspend = itemView.findViewById(R.id.userSuspend);
            userUnban = itemView.findViewById(R.id.userUnban);

            userReportedImg = itemView.findViewById(R.id.userReportedImg);
            userReportID = itemView.findViewById(R.id.userReportID);
            userReportStatus = itemView.findViewById(R.id.userReportStatus);
            userReportCategory = itemView.findViewById(R.id.userReportCategory);
            userReportDescription = itemView.findViewById(R.id.userReportDescription);
            userReportedName = itemView.findViewById(R.id.userReportedName);
            userReporterName = itemView.findViewById(R.id.userReporterName);


        }
    }
}