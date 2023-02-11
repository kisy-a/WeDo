package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.Calendar;
import java.util.HashMap;

public class BuyerOrderCheckout extends AppCompatActivity {

    private ImageView orderSum_serImage;
    private TextView orderSum_serSellerName, image_name, orderSum_buyerName, orderSum_serTitle, orderSum_price, orderSum_bankCompany, orderSum_bankOwner, orderSum_bankNumber, orderSum_sellerId;
    private RadioButton orderSum_radioOnlineBank;
    private RadioGroup btn_bankGrp;
    private ImageView orderSum_serSelleImage;

    private ImageButton CheckoutBackToSerDetail;
    private Button btn_purchaseConfirm, btn_chooseFile;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private EditText post_title, post_desc, post_budget, post_days,post_date, post_time ;
    private Button btnDatePicker, btnTimePicker;
    private String STitle, SImage, PDeliveryDate, PDeliveryTime, SDescription, SPrice, SDelivery, SCategory, UName, SBankName, SAccountNumber, SBeneficiaryName;
    private String serviceID, sellerID, sellerName,PaymentMethod, userID;

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

    //progress progress dialog
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_checkout);

        orderSum_serSelleImage = findViewById(R.id.orderSum_serSelleImage);
        orderSum_serSellerName = findViewById(R.id.orderSum_serSellerName);
        image_name = findViewById(R.id.image_name);
        orderSum_sellerId = findViewById(R.id.orderSum_sellerId);

        orderSum_serImage = findViewById(R.id.orderSum_serImage);
        orderSum_buyerName = findViewById(R.id.orderSum_buyerName);
        orderSum_serTitle = findViewById(R.id.orderSum_serTitle);
        orderSum_price = findViewById(R.id.orderSum_price);
        orderSum_bankOwner = findViewById(R.id.orderSum_bankOwner);
        orderSum_bankNumber = findViewById(R.id.orderSum_bankNumber);
        orderSum_radioOnlineBank = findViewById(R.id.orderSum_radioOnlineBank);
        btn_bankGrp = findViewById(R.id.btn_bankGrp);
        btn_chooseFile = findViewById(R.id.btn_chooseFile);

        post_title = findViewById(R.id.post_title);
        post_desc = findViewById(R.id.post_desc);
        post_budget = findViewById(R.id.post_budget);
        post_days = findViewById(R.id.post_days);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        post_date=(EditText)findViewById(R.id.post_date);
        post_time=(EditText)findViewById(R.id.post_time);
        CheckoutBackToSerDetail = findViewById(R.id.CheckoutBackToSerDetail);
        btn_purchaseConfirm = findViewById(R.id.btn_purchaseConfirm);

        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();

        //init permission array
        cameraPermission =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission =  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        serviceID = getIntent().getStringExtra("SID");
        sellerID = getIntent().getStringExtra("UID");
        loadServiceDetail();

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(BuyerOrderCheckout.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                post_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getButton(datePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                datePickerDialog.getButton(datePickerDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(BuyerOrderCheckout.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                post_time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                timePickerDialog.getButton(timePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                timePickerDialog.getButton(timePickerDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
            }
        });
        btn_chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog(); }
        });

        CheckoutBackToSerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent CheckBackToSerList = new Intent (BuyerOrderCheckout.this , BuyerServiceDetail.class);
                //startActivity(CheckBackToSerList);
            }
        });

        btn_purchaseConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();

            }
        });
    }

    private void loadServiceDetail() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.child(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String STitle = ""+snapshot.child("STitle").getValue();
                String SPrice = ""+snapshot.child("SPrice").getValue();
                SImage = ""+snapshot.child("SImage").getValue();
                String UName = ""+snapshot.child("UName").getValue();
                String SCategory = ""+snapshot.child("SCategory").getValue();
                String SDeliveryDays = ""+snapshot.child("SDeliveryDays").getValue();
                String UID = ""+snapshot.child("UID").getValue();

                //String SBankName = ""+snapshot.child("SBankName").getValue();
                //String SAccountNumber = ""+snapshot.child("SAccountNumber").getValue();
                //String SBeneficiaryName = ""+snapshot.child("SBeneficiaryName").getValue();

                //set data to text view
                orderSum_sellerId.setText(UID);
                orderSum_serTitle.setText(CensorWords.censor(STitle));
                orderSum_price.setText("PHP "+SPrice);

                orderSum_sellerId.setVisibility(View.INVISIBLE);

                try {
                    Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(orderSum_serImage);
                } catch (Exception e) {
                    orderSum_serImage.setImageResource(R.drawable.ic_no_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
        userReference.orderByChild("UID").equalTo(sellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();
                    String UImage = ""+ds.child("ProfileImage").getValue();

                    orderSum_serSellerName.setText(FullName);

                    try {
                        Picasso.get().load(UImage).placeholder(R.drawable.profile).into(orderSum_serSelleImage);
                    }
                    catch (Exception e) {
                        orderSum_serSelleImage.setImageResource(R.drawable.profile);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference bankReference = FirebaseDatabase.getInstance().getReference("BankInformation");
        bankReference.orderByChild("UID").equalTo(sellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()) {

                    String SBankName = ""+ds.child("SBankName").getValue();
                    String SAccountNumber = ""+ds.child("SAccountNumber").getValue();
                    String SBeneficiaryName = ""+ds.child("SBeneficiaryName").getValue();

                    orderSum_bankCompany.setText(SBankName);
                    orderSum_bankOwner.setText(SBeneficiaryName);
                    orderSum_bankNumber.setText(SAccountNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Users");
        dr.orderByChild("UID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String UName = "" + ds.child("FullName").getValue();

                    //set data to text view
                    orderSum_buyerName.setText(UName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void inputData() {
        PDeliveryDate = post_date.getText().toString().trim();
        PDeliveryTime = post_time.getText().toString().trim();

        STitle = orderSum_serTitle.getText().toString().trim();
        SPrice = orderSum_price.getText().toString().trim();
        UName = orderSum_buyerName.getText().toString().trim();
        sellerID = orderSum_sellerId.getText().toString().trim();

        if (orderSum_radioOnlineBank.isChecked()) {
            PaymentMethod = "Online Banking";
        }

        if(btn_bankGrp.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Payment Method is required.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(PDeliveryDate.isEmpty()){
            post_date.setError("Delivery Date is required.");
            return;
        }
        if(PDeliveryTime.isEmpty()){
            post_time.setError("Delivery Time is required.");
            return;
        }
        //checkAvailability();
        addOrder();
    }
    //to check avaialability of selected date and time
    private void checkAvailability(){
        //get orderid using sellerid and service id
        // then, gawin yung line 355 as databaseReference.child(orderid).addValueEventListener(new ValueEventListener()
        PDeliveryDate = post_date.getText().toString().trim();
        PDeliveryTime = post_time.getText().toString().trim();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String serviceid = snapshot.child("ServiceID").getValue().toString();
                String odate = snapshot.child("OrderDate").getValue().toString();
                String otime = snapshot.child("OrderTime").getValue().toString();
                String ostatus = snapshot.child("OStatus").getValue().toString();
                if (ostatus.equals("Pending")) {
                    if (serviceid.equals(serviceID) && odate.equals(PDeliveryDate) && otime.equals(PDeliveryTime)) {
                        addOrder();
                    }
                }else {
                    post_date.setError("Delivery Date is unavailable.");
                    post_time.setError("Delivery Time is unavailable.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void addOrder() {

        progressDialog.setMessage("Placing Order...");
        progressDialog.show();

        final String timestamp = "" + System.currentTimeMillis();
            //save checkout
        if (image_uri == null) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("OrderID", "" + timestamp);
            hashMap.put("OPaymentMethod", "" + PaymentMethod);
            hashMap.put("ServiceID", "" + serviceID);
            hashMap.put("BuyerID", "" + auth.getUid());
            hashMap.put("SellerID", "" + sellerID);
            hashMap.put("OrderDate", "" + PDeliveryDate);
            hashMap.put("OrderTime", "" + PDeliveryTime);
            hashMap.put("OStatus", "Pending");
            hashMap.put("AcceptedTime", "");
            hashMap.put("CompletedTime", "");
            hashMap.put("OImage", "");

            //add data to database
            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Tasks");
            dr.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Order Placed successfully.",Toast.LENGTH_SHORT).show();
                    clearData();
                    Intent intentToDoneBuy = new Intent (BuyerOrderCheckout.this , BuyerHome.class);
                    startActivity(intentToDoneBuy);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                }
            });
        }
        //add data w/ image
        else{
            String filePathAndName = "payment_images/" + "" + timestamp;

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
                        hashMap.put("OrderID", "" + timestamp);
                        hashMap.put("OImage", "" + downloadImageUri);
                        hashMap.put("OPaymentMethod", "" + PaymentMethod);
                        hashMap.put("ServiceID", "" + serviceID);
                        hashMap.put("BuyerID", "" + auth.getUid());
                        hashMap.put("SellerID", "" + sellerID);
                        hashMap.put("OrderDate", "" + PDeliveryDate);
                        hashMap.put("OrderTime", "" + PDeliveryTime);
                        hashMap.put("OStatus", "Pending");
                        hashMap.put("AcceptedTime", "");
                        hashMap.put("CompletedTime", "");

                        //add to database

                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Tasks");
                        dr.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Order Placed successfully.",Toast.LENGTH_SHORT).show();
                                clearData();
                                Intent intentToDoneBuy = new Intent (BuyerOrderCheckout.this , BuyerHome.class);
                                startActivity(intentToDoneBuy);
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
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

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        return c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE){

                //get chosen image
                image_uri = data.getData();
                String imageName = getFileName(image_uri);

                //set to image text view
                image_name.setText(imageName);

                //image_uri = data.getData();
                //image_name.setText(image_uri.getLastPathSegment());

                //set to image view
                //orderSum_buyerName.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){

                String imageName = getFileName(image_uri);
                image_name.setText(imageName);
                //set to image text view
                //image_name.setText(data.getData().getLastPathSegment());
                //set to image view
                //seller_serPhoto.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void clearData() {
        PDeliveryDate = post_date.getText().toString().trim();
        PDeliveryTime = post_time.getText().toString().trim();

        post_date.setText("");
        post_time.setText("");
    }
}