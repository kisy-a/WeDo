package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagePostingList extends AppCompatActivity {

    private TextView tv_NoProjectPosted;
    private ImageView iv_NoProjectPosted;

    private ImageButton backToSerList;
    private ArrayList<Model_JobPosting> PostingList;
    private RecyclerView recyclerViewManagePosting;
    private AdapterManagePosting adapterManagePosting;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_posting_list);

        tv_NoProjectPosted = findViewById(R.id.tv_NoProjectPosted);
        iv_NoProjectPosted = findViewById(R.id.iv_NoProjectPosted);
        backToSerList =findViewById(R.id.backToSerList);
        recyclerViewManagePosting = findViewById(R.id.recyclerViewManagePosting);

        auth = FirebaseAuth.getInstance();

       // recyclerViewManagePosting.setHasFixedSize(true);

        //adapterManagePosting = new AdapterManagePosting(this,PostingList);
        //recyclerViewManagePosting.setLayoutManager(new LinearLayoutManager(this));

       // recyclerViewManagePosting.setAdapter(adapterManagePosting);
        loadAllProject();

        backToSerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                Intent back2acc = new Intent(ManagePostingList.this, BuyerAccount.class);
                startActivity(back2acc);
            }
        });

    }

    private void loadAllProject() {
        PostingList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        //method 1
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Project");
        databaseReference.orderByChild("UID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostingList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_JobPosting model_jobPosting = ds.getValue(Model_JobPosting.class);
                    PostingList.add(model_jobPosting);
                }

                recyclerViewManagePosting.setHasFixedSize(true);
                //setup adapter
                adapterManagePosting = new AdapterManagePosting(ManagePostingList.this, PostingList);
                //set adapter
                recyclerViewManagePosting.setAdapter(adapterManagePosting);
                adapterManagePosting.notifyDataSetChanged();

                if(PostingList.isEmpty()){
                    tv_NoProjectPosted.setVisibility(View.VISIBLE);
                    iv_NoProjectPosted.setVisibility(View.VISIBLE);
                }

                //String UID = ""+ds.child("UID").getValue();
                //  if(!UID.equals(auth.getUid())) {
                // BrowseJobList.add(model_jobPosting);
                // }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //method 2

        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        //databaseReference.child(auth.getUid()).child("Project").addValueEventListener(new ValueEventListener() {
            //@Override
            //public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for (DataSnapshot ds: snapshot.getChildren()){
                    //Model_JobPosting model_jobPosting = ds.getValue(Model_JobPosting.class);
                    //PostingList.add(model_jobPosting);
                //}
                //setup adapter
                //adapterManagePosting = new AdapterManagePosting(ManagePostingList.this, PostingList);
                //set adapter
                //recyclerViewManagePosting.setAdapter(adapterManagePosting);
            //}

            //@Override
            //public void onCancelled(@NonNull DatabaseError error) {

            //}
        //});

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}