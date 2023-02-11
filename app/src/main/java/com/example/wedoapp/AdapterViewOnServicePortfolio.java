package com.example.wedoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterViewOnServicePortfolio extends RecyclerView.Adapter<AdapterViewOnServicePortfolio.HolderOnServicePortfolio> {

    private Context context;
    private ArrayList<Model_PortfolioDesign> OnServicePortfolioList;

    public AdapterViewOnServicePortfolio(Context context, ArrayList<Model_PortfolioDesign>onnServicePortfolioList){
        this.context = context;
        this.OnServicePortfolioList = onnServicePortfolioList;
    }
    @NonNull
    @Override
    public AdapterViewOnServicePortfolio.HolderOnServicePortfolio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_portfolio,parent,false);
        return new AdapterViewOnServicePortfolio.HolderOnServicePortfolio(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewOnServicePortfolio.HolderOnServicePortfolio holder, int position) {
        Model_PortfolioDesign model_portfolioDesign = OnServicePortfolioList.get(position);

        String ImageSample = model_portfolioDesign.getImageSample();
        String PortfolioID = model_portfolioDesign.getPortfolioID();

        try {
            Picasso.get().load(ImageSample).placeholder(R.drawable.ic_no_image).into(holder.ViewOnServiceDesignImage);
        }
        catch (Exception e) {
            holder.ViewOnServiceDesignImage.setImageResource(R.drawable.ic_no_image);
        }



    }

    @Override
    public int getItemCount() {
        int limit = 5;
        if(OnServicePortfolioList.size() > limit){
            return limit;
        }
        else
        {
            return OnServicePortfolioList.size();
        }
        //return OnServicePortfolioList.size();
    }

    public class HolderOnServicePortfolio extends RecyclerView.ViewHolder{

        ImageView ViewOnServiceDesignImage;

        public HolderOnServicePortfolio(@NonNull View itemView) {
            super(itemView);

            ViewOnServiceDesignImage = itemView.findViewById(R.id.ViewOnServiceDesignImage);

        }
    }
}
