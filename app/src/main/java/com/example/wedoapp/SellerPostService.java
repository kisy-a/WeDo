package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SellerPostService extends AppCompatActivity {

    private EditText seller_serviceTitle, seller_serviceDesc, seller_servicePrice, seller_serviceDays;
    private RadioGroup ser_categoryGroup;
    private RadioButton seller_serRadioCatering, seller_serRadioHair,seller_serRadioFlower, seller_serRadioVenue, seller_serRadioCar, seller_serRadioDress, seller_serRadioMedia;
    private ImageView seller_serPhoto;
    private ImageButton PostBackToSellerAccount;
    private Button btn_SerPublish;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String STitle, SDescription, SPrice, SDelivery, SCategory, UName, SBankName, SAccountNumber, SBeneficiaryName, SellerImage;

    //progress progress dialog
    private ProgressDialog progressDialog;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //permission array
    private String[] cameraPermission;
    private String[] storagePermission;
    //image picked uri
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_post_service);

        seller_serviceTitle = findViewById(R.id.seller_serviceTitle);
        seller_serviceDesc = findViewById(R.id.seller_serviceDesc);
        seller_servicePrice = findViewById(R.id.seller_servicePrice);
        seller_serviceDays = findViewById(R.id.seller_serviceDays);
        ser_categoryGroup = findViewById(R.id.ser_categoryGroup);
        seller_serRadioCatering = findViewById(R.id.seller_serRadioCatering);
        seller_serRadioHair = findViewById(R.id.seller_serRadioHair);
        seller_serRadioVenue = findViewById(R.id.seller_serRadioVenue);
        seller_serRadioCar = findViewById(R.id.seller_serRadioCar);
        seller_serRadioDress = findViewById(R.id.seller_serRadioDress);
        seller_serRadioMedia = findViewById(R.id.seller_serRadioMedia);
        seller_serRadioFlower = findViewById(R.id.seller_serRadioFlower);
        seller_serPhoto = findViewById(R.id.seller_serPhoto);

        PostBackToSellerAccount = findViewById(R.id.PostBackToSellerAccount);
        btn_SerPublish = findViewById(R.id.btn_SerPublish);

        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();

        //init permission array
        cameraPermission =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission =  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        seller_serPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog(); }
        });

        PostBackToSellerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent PostBack2Acc= new Intent(SellerPostService.this, SellerAccount.class);
                //startActivity(PostBack2Acc);
            }
        });

        btn_SerPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputService();

            }
        });
    }

    private void inputService() {

        STitle = seller_serviceTitle.getText().toString().trim();
        SDescription = seller_serviceDesc.getText().toString().trim();
        SPrice = seller_servicePrice.getText().toString().trim();
        SDelivery = seller_serviceDays.getText().toString().trim();

        if (seller_serRadioHair.isChecked()) {
            SCategory = "Hair and Makeup";
        } else if (seller_serRadioVenue.isChecked()) {
            SCategory = "Venue";
        } else if (seller_serRadioDress.isChecked()) {
            SCategory = "Dress Tailoring";
        } else if (seller_serRadioMedia.isChecked()) {
            SCategory = "Audio/Video";
        } else if (seller_serRadioCar.isChecked()) {
            SCategory = "Car Rental";
        } else if (seller_serRadioCatering.isChecked()) {
            SCategory = "Catering Services";
        } else if (seller_serRadioFlower.isChecked()) {
            SCategory = "Flowers";
        }

        if(STitle.isEmpty()){
            seller_serviceTitle.setError("Title is required.");
            return;
        }
        if(SDescription.isEmpty()){
            seller_serviceDesc.setError("Description is required.");
            return;
        }
        if(SPrice.isEmpty()){
            seller_servicePrice.setError("Price is required.");
            return;
        }
        if(SDelivery.isEmpty()){
            seller_serviceDays.setError("Delivery Hours is required.");
            return;
        }

        if(ser_categoryGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Category is required.",Toast.LENGTH_SHORT).show();
            return;
        }

        if (image_uri == null){
            Toast.makeText(getApplicationContext(),"Service Image is required.",Toast.LENGTH_SHORT).show();
            return;
        }

        addService();
    }

    private void addService() {
        progressDialog.setMessage("Posting Service...");
        progressDialog.show();

        final String timestamp = "" + System.currentTimeMillis();
        if (image_uri==null) {
            //upload product without image

            //setup service data to database
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("SID", "" + timestamp);
            hashMap.put("SImage", ""); //if not image,set it empty
            hashMap.put("STitle", "" + STitle);
            hashMap.put("SDescription", "" + SDescription);
            hashMap.put("SPrice", "" + SPrice);
            hashMap.put("SDeliveryDays", "" + SDelivery);
            hashMap.put("SCategory", "" + SCategory);
            hashMap.put("UID", "" + auth.getUid());

            //save to database

            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Services");
            dr.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Service added successfully.",Toast.LENGTH_SHORT).show();
                    clearData();

                    Intent intentToSellerAcc = new Intent (SellerPostService.this , SellerAccount.class);
                    startActivity(intentToSellerAcc);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {

            //upload product with image

            String filePathAndName = "service_images/" + "" + timestamp;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadImageUri = uriTask.getResult();

                    if (uriTask.isSuccessful()){
                        //setup service data to upload
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("SID", "" + timestamp);
                        hashMap.put("SImage", "" + downloadImageUri);
                        hashMap.put("STitle", "" + STitle);
                        hashMap.put("SDescription", "" + SDescription);
                        hashMap.put("SPrice", "" + SPrice);
                        hashMap.put("SDeliveryDays", "" + SDelivery);
                        hashMap.put("SCategory", "" + SCategory);
                        hashMap.put("UID", "" + auth.getUid());

                        //add to database

                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Services");
                        dr.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Service added successfully.",Toast.LENGTH_SHORT).show();
                                clearData();

                                Intent intentToSellerAcc = new Intent (SellerPostService.this , SellerAccount.class);
                                startActivity(intentToSellerAcc);
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void clearData() {

        STitle = seller_serviceTitle.getText().toString().trim();
        SDescription = seller_serviceDesc.getText().toString().trim();
        SPrice = seller_servicePrice.getText().toString().trim();
        SDelivery = seller_serviceDays.getText().toString().trim();

        seller_serviceTitle.setText("");
        seller_serviceDesc.setText("");
        seller_servicePrice.setText("");
        seller_serviceDays.setText("");
        seller_serPhoto.setImageResource(R.drawable.ic_add_photo);
        image_uri = null;
    }

    private void showImageDialog() {
        //options to display in dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item clicks
                if (which==0){
                    //camera clicked
                    if (checkCameraPermission()){
                        //allowed, open camera
                        pickFromCamera();
                    }
                    else {
                        requestCameraPermission();
                    }
                }
                else {
                    //gallery clicked
                    if (checkStoragePermission()){
                        //allowed, open gallery
                        pickFromGallery();
                    }
                    else
                        requestStoragePermission();
                }
            }
        }).show();

    }

    private void pickFromGallery() {
        Intent intentToGallery = new Intent(Intent.ACTION_GET_CONTENT);
        intentToGallery.setType("image/*");
        startActivityForResult(intentToGallery, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        //intent to pick image from camera
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentToCamera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intentToCamera, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Camera Permissions are necessary.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Storage Permissions are necessary.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                //get chosen image
                image_uri = data.getData();
                //set to image view
                seller_serPhoto.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to image view
                seller_serPhoto.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}