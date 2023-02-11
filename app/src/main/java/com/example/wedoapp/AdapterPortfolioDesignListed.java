package com.example.wedoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterPortfolioDesignListed extends RecyclerView.Adapter<AdapterPortfolioDesignListed.HolderPortfolioDesignListed> {

    private Context context;
    private ArrayList<Model_PortfolioDesign> ServicePortfolioSampleList;

    public AdapterPortfolioDesignListed(Context context, ArrayList<Model_PortfolioDesign>servicePortfolioSampleList){
        this.context = context;
        this.ServicePortfolioSampleList = servicePortfolioSampleList;
    }
    @NonNull
    @Override
    public AdapterPortfolioDesignListed.HolderPortfolioDesignListed onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio_sample_view,parent,false);
        return new AdapterPortfolioDesignListed.HolderPortfolioDesignListed(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPortfolioDesignListed.HolderPortfolioDesignListed holder, int position) {
        Model_PortfolioDesign model_portfolioDesign = ServicePortfolioSampleList.get(position);

        String ImageSample = model_portfolioDesign.getImageSample();
        String PortfolioID = model_portfolioDesign.getPortfolioID();

        try {
            Picasso.get().load(ImageSample).placeholder(R.drawable.ic_no_image).into(holder.ViewDesignImage);
        }
        catch (Exception e) {
            holder.ViewDesignImage.setImageResource(R.drawable.ic_no_image);
        }

        holder.btn_deleteDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();

                String PortfolioID = model_portfolioDesign.getPortfolioID();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Portfolio");
                databaseReference.child(PortfolioID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Deleted.",Toast.LENGTH_SHORT).show();
                        //ServicePortfolioSampleList.remove(position);
                        notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

              /*  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
               databaseReference.child(orderID).child("OStatus").setValue(oStatus);

                */

                //Toast.makeText(context,"Order Completed",Toast.LENGTH_SHORT).show();

                //Intent sIntentReturnTaskList = new Intent(context, Orders.class);
               // context.startActivity(sIntentReturnTaskList);

            }
        });

       /* DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Services");
        dr.child(serviceID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Deleted.",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });*/

    }


    @Override
    public int getItemCount() {
        return ServicePortfolioSampleList.size();
    }

    public class HolderPortfolioDesignListed extends RecyclerView.ViewHolder{

        Button btn_deleteDesign;
        ImageView ViewDesignImage;

        public HolderPortfolioDesignListed(@NonNull View itemView) {
            super(itemView);

            btn_deleteDesign = itemView.findViewById(R.id.btn_deleteDesign);
            ViewDesignImage = itemView.findViewById(R.id.ViewDesignImage);

        }
    }
}

