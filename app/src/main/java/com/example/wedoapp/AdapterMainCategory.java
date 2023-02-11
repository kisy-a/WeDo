package com.example.wedoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMainCategory extends RecyclerView.Adapter<AdapterMainCategory.HolderMainCategory> {

    private Context context;
    private ArrayList<Model_MainCategory> MainCategoryList;

    public AdapterMainCategory(Context context, ArrayList<Model_MainCategory> mainCategoryList){
        this.context = context;
        this.MainCategoryList = mainCategoryList;
    }
    @NonNull
    @Override
    public AdapterMainCategory.HolderMainCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_main_category,parent,false);
        return new AdapterMainCategory.HolderMainCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMainCategory.HolderMainCategory holder, @SuppressLint("RecyclerView") int position) {
        Model_MainCategory model_mainCategory = MainCategoryList.get(position);

          holder.MainCat_Title.setText(model_mainCategory.getCategoryName());
          holder.MainCat_Desc.setText(model_mainCategory.getCategoryDesc());
          holder.MainCat_SerImage.setImageResource(model_mainCategory.getCategoryImage());

          holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                    //hair
                    Intent intent = new Intent(context, ServiceCategoryHair.class);
                    context.startActivity(intent);
                }else if(position==1){
                    //dress
                    Intent intent1 = new Intent(context, ServiceCategoryDress.class);
                    context.startActivity(intent1);
                }else if(position==2){
                    //car
                    Intent intent2 = new Intent(context, ServiceCategoryCar.class);
                    context.startActivity(intent2);
                }else if(position==3){
                    //venue
                    Intent intent3 = new Intent(context, ServiceCategoryVenue.class);
                    context.startActivity(intent3);
                }else if(position==4){
                    //audio video
                    Intent intent4 = new Intent(context, ServiceCategoryAudioVideo.class);
                    context.startActivity(intent4);
                }else if(position==5){
                    //catering
                    Intent intent5 = new Intent(context, ServiceCategoryCatering.class);
                    context.startActivity(intent5);
                }else if(position==6){
                    //flower services
                    Intent intent5 = new Intent(context, ServiceCategoryFlower.class);
                    context.startActivity(intent5);
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return MainCategoryList.size();
    }


    public class HolderMainCategory extends RecyclerView.ViewHolder{

        //hold view of recycler view
        ImageView MainCat_SerImage;
        TextView MainCat_Desc,MainCat_Title;

        public HolderMainCategory(@NonNull View itemView) {
            super(itemView);

            MainCat_SerImage = itemView.findViewById(R.id.MainCat_SerImage);
            MainCat_Desc = itemView.findViewById(R.id.MainCat_Desc);
            MainCat_Title = itemView.findViewById(R.id.MainCat_Title);


        }



    }
}
