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

public class AdapterPortfolioDesign extends RecyclerView.Adapter<AdapterPortfolioDesign.HolderPortfolioDesign> {

    private Context context;
    private ArrayList<Model_PortfolioDesign> PortfolioDesignList;

    public AdapterPortfolioDesign(Context context, ArrayList<Model_PortfolioDesign> portfolioDesignList){
        this.context = context;
        this.PortfolioDesignList = portfolioDesignList;
    }
    @NonNull
    @Override
    public AdapterPortfolioDesign.HolderPortfolioDesign onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio_sample,parent,false);
        return new AdapterPortfolioDesign.HolderPortfolioDesign(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPortfolioDesign.HolderPortfolioDesign holder, int position) {
        Model_PortfolioDesign model_portfolioDesign = PortfolioDesignList.get(position);

        //String ImageName = model_portfolioDesign.getImageName();
        String ImageSample = model_portfolioDesign.getImageSample();


        try {
            Picasso.get().load(ImageSample).placeholder(R.drawable.ic_no_image).into(holder.DesignImage);
        }
        catch (Exception e) {
            holder.DesignImage.setImageResource(R.drawable.ic_no_image);
        }

        /*holder.imageDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, PortfolioDesignList.get(position).getImageName()+"Removed", Toast.LENGTH_SHORT).show();
                PortfolioDesignList.remove(position);
                notifyDataSetChanged();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return PortfolioDesignList.size();
    }

    public class HolderPortfolioDesign extends RecyclerView.ViewHolder{

        ImageView DesignImage;
        //TextView DesignName;
        //FloatingActionButton imageDel;

        public HolderPortfolioDesign(@NonNull View itemView) {
            super(itemView);

            //imageDel = itemView.findViewById(R.id.imageDel);
            DesignImage = itemView.findViewById(R.id.DesignImage);
            //DesignName = itemView.findViewById(R.id.DesignName);

        }
    }
}
