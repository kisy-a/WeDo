package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class AddSamplePortfolio extends AppCompatActivity {

    private TextView tv_AlertNoDesign;
    ImageButton AddPortfolioBackToList;
    ArrayList<Uri> PortfolioDesignList = new ArrayList<Uri>();
    private ArrayList<Model_PortfolioDesign> DesignList ;
    private AdapterPortfolioDesign adapterPortfolioDesign;
    private Button btn_uploadDesign, btn_addDesign;
    private RecyclerView recyclerViewPortfolioDesign;
    private ArrayList savedImagesUri;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String serviceID;
    private int uploads = 0;

    //progress progress dialog
    private ProgressDialog progressDialog;
    //permission constants
    private static final int STORAGE_REQUEST_CODE = 1;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 2;
    //permission array
    private String[] cameraPermission;
    private String[] storagePermission;
    //image picked uri
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sample_portfolio);

        btn_addDesign = findViewById(R.id.btn_addDesign);
        tv_AlertNoDesign = findViewById(R.id.tv_AlertNoDesign);
        AddPortfolioBackToList = findViewById(R.id.AddPortfolioBackToList);
        btn_uploadDesign = findViewById(R.id.btn_uploadDesign);
        recyclerViewPortfolioDesign = findViewById(R.id.recyclerViewPortfolioDesign);

        auth = FirebaseAuth.getInstance();
        serviceID = getIntent().getStringExtra("SID");

        DesignList = new ArrayList<>();

        adapterPortfolioDesign = new AdapterPortfolioDesign(AddSamplePortfolio.this, DesignList);
        recyclerViewPortfolioDesign.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPortfolioDesign.setAdapter(adapterPortfolioDesign);

        adapterPortfolioDesign.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adapterPortfolioDesign.getItemCount() !=0){
                    tv_AlertNoDesign.setVisibility(View.GONE);
                }
                else{
                    tv_AlertNoDesign.setVisibility(View.VISIBLE);
                }
            }
        });

        AddPortfolioBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Portfolio");

        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void choose(View view) {
        //we will pick images
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_GALLERY_CODE) {
            if (resultCode == RESULT_OK && data !=null) {
                ClipData clipData = data.getClipData();
                if (clipData!= null) {

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        String image_uri = String.valueOf(clipData.getItemAt(i).getUri());
                        String ImageName = getFileNameFromUri(Uri.parse(image_uri));
                        PortfolioDesignList.add(Uri.parse(image_uri));

                        DesignList.add(new Model_PortfolioDesign(getFileNameFromUri(Uri.parse(image_uri)),image_uri));
                        adapterPortfolioDesign.notifyDataSetChanged();
                    }
                } else {
                    String image_uri = String.valueOf(data.getData());
                    String ImageName = getFileNameFromUri(Uri.parse(image_uri));
                    Model_PortfolioDesign model_portfolioDesign = new Model_PortfolioDesign(ImageName,image_uri);
                    PortfolioDesignList.add(Uri.parse(image_uri));
                    DesignList.add(new Model_PortfolioDesign(getFileNameFromUri(Uri.parse(image_uri)), image_uri));
                    adapterPortfolioDesign.notifyDataSetChanged();
                }

            }

        }

    }

    @SuppressLint("SetTextI18n")
    public void upload(View view) {

        if(PortfolioDesignList.size() !=0){
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child("portfolio_images/");

            for (uploads=0; uploads < PortfolioDesignList.size(); uploads++) {
                final int finalImage = uploads;


                Uri Image  = PortfolioDesignList.get(uploads);
                final StorageReference imagename = ImageFolder.child(Image.getLastPathSegment());

                imagename.putFile(PortfolioDesignList.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressDialog.dismiss();
                                //counter++;
                                //progressDialog.setMessage("Uploaded"+counter+"/"+PortfolioDesignList.size());
                                String url = String.valueOf(uri);
                                SaveToDatabase(url);
                            }
                        });

                    }
                });
        }

        }else{
            Toast.makeText(getApplicationContext(),"Please add at least one image.",Toast.LENGTH_SHORT).show();
        }


    }

    private void SaveToDatabase(String ImageSample) {
        progressDialog.dismiss();
        final String timestamp = "" + System.currentTimeMillis();
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("PortfolioID", "" + timestamp);
        hashMap.put("ImageSample",  "" + ImageSample);
        hashMap.put("ServiceID",  "" + serviceID);

        /*databaseReference.child(serviceID).child("Portfolio").child(timestamp).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                //textView.setText("Image Uploaded Successfully");
                PortfolioDesignList.clear();
                Intent backToManageService = new Intent(AddSamplePortfolio.this, ManageServiceList.class);
                startActivity(backToManageService);
                finish();
            }
        });*/

        databaseReference.child(timestamp).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                PortfolioDesignList.clear();
                finish();
            }
        });


    }


    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}