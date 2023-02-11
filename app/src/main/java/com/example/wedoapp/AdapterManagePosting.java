package com.example.wedoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterManagePosting extends RecyclerView.Adapter<AdapterManagePosting.HolderManagePosting> {

    private Context context;
    private ArrayList<Model_JobPosting> PostingList;

   public AdapterManagePosting(Context context, ArrayList<Model_JobPosting> postingList){
        this.context = context;
        this.PostingList = postingList;
    }
    @NonNull
    @Override
    public AdapterManagePosting.HolderManagePosting onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posting_list_buyer,parent,false);
        return new HolderManagePosting(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderManagePosting holder, int position) {
       //get data
        Model_JobPosting model_jobPosting = PostingList.get(position);

        String projectID = model_jobPosting.getProjectID();
        String uid = model_jobPosting.getUID();
        String title = model_jobPosting.getPTitle();
        String description = model_jobPosting.getPDescription();
        String category = model_jobPosting.getPCategory();
        String budget = model_jobPosting.getPBudget();
        String deliveryDays = model_jobPosting.getPDeliveryDays();
        String deliveryDate = model_jobPosting.getPDeliveryDate();
        String deliveryTime = model_jobPosting.getPDeliveryTime();
        String status = model_jobPosting.getPStatus();

        //set data
        holder.managePosting_serTitle.setText(title);
        holder.managePosting_serStatus.setText(status);
        holder.managePosting_serDesc.setText(description);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show item details

                Intent intentViewProject = new Intent(context, BuyerProjectDetail.class);
                intentViewProject.putExtra("ProjectID", projectID);
                context.startActivity(intentViewProject);
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

    @Override
    public int getItemCount() {
        return PostingList.size();
    }

    public class HolderManagePosting extends RecyclerView.ViewHolder{
        //hold view of recycler view

        private TextView managePosting_serTitle, managePosting_serStatus, managePosting_serDesc;

        public HolderManagePosting(@NonNull View itemView) {
            super(itemView);

            managePosting_serTitle = itemView.findViewById(R.id.managePosting_serTitle);
            managePosting_serStatus = itemView.findViewById(R.id.managePosting_serStatus);
            managePosting_serDesc = itemView.findViewById(R.id.managePosting_serDesc);


        }
    }
}
