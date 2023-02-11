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

public class AdapterBrowseJob extends RecyclerView.Adapter<AdapterBrowseJob.HolderBrowseJob> {

    private Context context;
    private ArrayList<Model_JobPosting> BrowseJobList;

    public AdapterBrowseJob(Context context, ArrayList<Model_JobPosting> browseJobList){
        this.context = context;
        this.BrowseJobList = browseJobList;
    }
    @NonNull
    @Override
    public AdapterBrowseJob.HolderBrowseJob onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_list_seller,parent,false);
        return new AdapterBrowseJob.HolderBrowseJob(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBrowseJob.HolderBrowseJob holder, int position) {
        //get data
        Model_JobPosting model_jobPosting = BrowseJobList.get(position);

        String projectID = model_jobPosting.getProjectID();
        String userID = model_jobPosting.getUID();
        String title = model_jobPosting.getPTitle();
        String description = model_jobPosting.getPDescription();
        String category = model_jobPosting.getPCategory();
        String budget = model_jobPosting.getPBudget();
        String deliveryDays = model_jobPosting.getPDeliveryDays();
        String deliveryDate = model_jobPosting.getPDeliveryDate();
        String deliveryTime = model_jobPosting.getPDeliveryTime();
        String status = model_jobPosting.getPStatus();

        //set data
        holder.browse_jobTitle.setText(CensorWords.censor(title));
        holder.browse_jobCategory.setText(category);
        holder.browse_jobPrice.setText("PHP " + budget);
        holder.browse_jobDesc.setText(CensorWords.censor(description));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show item details

                Intent intentViewProject = new Intent(context, SellerJobDetails.class);
                intentViewProject.putExtra("ProjectID", projectID);
                intentViewProject.putExtra("UID", userID);
                context.startActivity(intentViewProject);
            }
        });

       // holder.browse_jobTitle.setText(currentItem.getTitle());
       // holder.browse_jobCategory.setText(currentItem.getCategory());
       // holder.browse_jobPrice.setText(currentItem.getPrice());
      //  holder.browse_jobDesc.setText(currentItem.getDesc());

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
        return BrowseJobList.size();
    }
    
    public class HolderBrowseJob extends RecyclerView.ViewHolder{

        //hold view of recycler view
        TextView browse_jobTitle,browse_jobCategory,browse_jobPrice,browse_jobDesc;

        public HolderBrowseJob(@NonNull View itemView) {
            super(itemView);
            browse_jobTitle = itemView.findViewById(R.id.browse_jobTitle);
            browse_jobCategory = itemView.findViewById(R.id.browse_jobCategory);
            browse_jobPrice = itemView.findViewById(R.id.browse_jobPrice);
            browse_jobDesc = itemView.findViewById(R.id.browse_jobDesc);


        }
    }
}