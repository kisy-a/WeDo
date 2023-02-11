package com.example.wedoapp;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterManageService extends RecyclerView.Adapter<AdapterManageService.HolderManageService> {

    private Context context;
    private ArrayList<Model_Service> ManageServiceList;

    public AdapterManageService(Context context, ArrayList<Model_Service> manageServiceList){
        this.context = context;
        this.ManageServiceList = manageServiceList;
    }
    @NonNull
    @Override
    public AdapterManageService.HolderManageService onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list_seller,parent,false);
        return new AdapterManageService.HolderManageService(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterManageService.HolderManageService holder, int position) {
        Model_Service model_service = ManageServiceList.get(position);

        String serviceID = model_service.getSID();
        String uid = model_service.getUID();
        String sTitle = model_service.getSTitle();
        String sDescription = model_service.getSDescription();
        String sCategory = model_service.getSCategory();
        String sPrice = model_service.getSPrice();
        String sDeliveryDays = model_service.getSDeliveryDays();
        String sImage = model_service.getSImage();

        //set data
        holder.manageSer_Title.setText(CensorWords.censor(sTitle));
        holder.manageSer_Category.setText(CensorWords.censor(sCategory));
        holder.manageSer_price.setText("PHP " + sPrice);

        try {
            Picasso.get().load(sImage).placeholder(R.drawable.ic_no_image).into(holder.manageSer_Image);
        }
        catch (Exception e) {
            holder.manageSer_Image.setImageResource(R.drawable.ic_no_image);
        }

        //view detail
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show item details
                Intent sIntentViewsService = new Intent(context, SellerServiceDetails.class);
                sIntentViewsService.putExtra("SID", serviceID);
                context.startActivity(sIntentViewsService);
            }
        });

        //view review
        holder.btn_viewReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sIntentViewsReview = new Intent(context, SellerViewReview.class);
                sIntentViewsReview.putExtra("SID", serviceID);
                context.startActivity(sIntentViewsReview);

            }
        });

        //edit service
        holder.btn_editSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sIntentEditSer = new Intent(context, EditService.class);
                sIntentEditSer.putExtra("SID", serviceID);
                context.startActivity(sIntentEditSer);

            }
        });

        //view portfolio
        holder.btn_portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sIntentViewPortfolio = new Intent(context, ViewPortfolio.class);
                sIntentViewPortfolio.putExtra("SID", serviceID);
                context.startActivity(sIntentViewPortfolio);

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
        return ManageServiceList.size();
    }

    public class HolderManageService extends RecyclerView.ViewHolder{

        Button btn_viewReview, btn_editSer, btn_portfolio;
        ImageView manageSer_Image;
        TextView manageSer_Category,manageSer_Title,manageSer_price;

        public HolderManageService(@NonNull View itemView) {
            super(itemView);

            btn_portfolio = itemView.findViewById(R.id.btn_portfolio);
            btn_editSer = itemView.findViewById(R.id.btn_editSer);
            btn_viewReview = itemView.findViewById(R.id.btn_viewReview);
            manageSer_Image = itemView.findViewById(R.id.manageSer_Image);
            manageSer_Category = itemView.findViewById(R.id.manageSer_Category);
            manageSer_Title = itemView.findViewById(R.id.manageSer_Title);
            manageSer_price = itemView.findViewById(R.id.manageSer_price);

        }
    }
}
