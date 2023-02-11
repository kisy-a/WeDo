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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    private EditText signUp_fullName, signUp_emailAdd, signUp_HP, signUp_location, signUp_password, signUp_confPassword;
    private ImageButton backToLogin;
    private Button btn_signUp;
    private TextView LogIn, user_type;
    private ImageView addProfilePic;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String FullName, EmailAddress, PhoneContact, Location, Password, ConfirmPassword;

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
        setContentView(R.layout.activity_sign_up);

        signUp_fullName = findViewById(R.id.signUp_fullName);
        signUp_emailAdd = findViewById(R.id.signUp_emailAdd);
        signUp_HP = findViewById(R.id.signUp_HP);
        signUp_location = findViewById(R.id.signUp_location);
        signUp_password = findViewById(R.id.signUp_password);
        signUp_confPassword = findViewById(R.id.signUp_confPassword);
        addProfilePic = findViewById(R.id.addProfilePic);

        btn_signUp = findViewById(R.id.btn_signUp);
        backToLogin = findViewById(R.id.backToLogin);
        LogIn = findViewById(R.id.LogIn);
        //get user type
        user_type = findViewById(R.id.received_value_id);
        Intent intent = getIntent();
        String str = intent.getStringExtra("userType");
        user_type.setText(str);
        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();

        //init permission array
        cameraPermission =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission =  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        addProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog(); }
        });


        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2LogIn = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent2LogIn);
            }
        });
    }

    private void inputData() {
        FullName = signUp_fullName.getText().toString().trim();
        EmailAddress = signUp_emailAdd.getText().toString().trim();
        PhoneContact = signUp_HP.getText().toString().trim();
        Location = signUp_location.getText().toString().trim();
        Password = signUp_password.getText().toString().trim();
        ConfirmPassword = signUp_confPassword.getText().toString().trim();

        if(FullName.isEmpty()){
            signUp_fullName.setError("Full Name is required.");
            return;
        }
        if(EmailAddress.isEmpty()){
            signUp_emailAdd.setError("Email Address is required.");
            return;
        }
        if(PhoneContact.isEmpty()){
            signUp_HP.setError("Contact Number is required.");
            return;
        }
        if(Location.isEmpty()){
            signUp_location.setError("Location is required.");
            return;
        }
        if(Password.isEmpty()){
            signUp_password.setError("Password is required.");
            return;
        }
        if(ConfirmPassword.isEmpty()){
            signUp_confPassword.setError("Confirm Password is required.");
            return;
        }

        if(!Password.equals(ConfirmPassword)) {
            signUp_confPassword.setError("Password does not match.");
            return;
        }
        createAccount();

    }

    private void createAccount() {

        auth.createUserWithEmailAndPassword(EmailAddress, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                saveAccountData();
                signUp_fullName.getText().clear();
                signUp_emailAdd.getText().clear();
                signUp_HP.getText().clear();
                signUp_location.getText().clear();
                signUp_password.getText().clear();
                signUp_confPassword.getText().clear();
                user_type.setText("");
                //email Verification Notice Sent
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUp.this, "Email Verification have been sent.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAccountData() {
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        String type = user_type.getText().toString().trim();
        //add data w/o image
        if (image_uri == null) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("UID", "" + auth.getUid());
            hashMap.put("FullName", "" + FullName);
            hashMap.put("EmailAddress", "" + EmailAddress);
            hashMap.put("PhoneContact", "" + PhoneContact);
            hashMap.put("Location", "" + Location);
            hashMap.put("Password", "" + Password);
            hashMap.put("ConfirmPassword", "" + ConfirmPassword);
            hashMap.put("userType", "" + type);
            hashMap.put("OnlineStatus", "");

            //add data to database
            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("Users");
            databaseReference.child(auth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    //Toast.makeText(SignUp.this, "Register Successfully!", Toast.LENGTH_SHORT).show();

                    if (auth.getCurrentUser().isEmailVerified()) {
                        if (type.equals("buyer")){
                            Intent intent2BuyerHome = new Intent(SignUp.this, BuyerHome.class);
                            startActivity(intent2BuyerHome);
                            finish();
                        }
                        else {
                            //seller 'home'
                            Intent intent2SellerHome = new Intent(SignUp.this, BrowseJobList.class);
                            startActivity(intent2SellerHome);
                            finish();
                        }

                    } else {
                        Toast.makeText(SignUp.this, "Check your email for verification.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), EmailVerification.class));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "Register Unsuccessfully.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        //add data w/ image
        else{
            String filePathAndName = "profile_images/" + "" + auth.getUid();
            storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //get uri of uploaded image
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadImageUri = uriTask.getResult();

                    if (uriTask.isSuccessful()){
                        //set data to save
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("UID", "" + auth.getUid());
                        hashMap.put("FullName", "" + FullName);
                        hashMap.put("EmailAddress", "" + EmailAddress);
                        hashMap.put("PhoneContact", "" + PhoneContact);
                        hashMap.put("Location", "" + Location);
                        hashMap.put("Password", "" + Password);
                        hashMap.put("ConfirmPassword", "" + ConfirmPassword);
                        hashMap.put("ProfileImage", "" +downloadImageUri); //uri of uploaded image
                        hashMap.put("OnlineStatus", "");

                        //save to database
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        databaseReference.child(auth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(SignUp.this, "Register Successfully!", Toast.LENGTH_SHORT).show();
                                if (auth.getCurrentUser().isEmailVerified()) {
                                    progressDialog.dismiss();
                                    Intent intent2customer_main = new Intent(SignUp.this, BuyerHome.class);
                                    startActivity(intent2customer_main);
                                    finish();
                                } else {
                                    startActivity(new Intent(getApplicationContext(), EmailVerification.class));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this, "Register Unsuccessfully.", Toast.LENGTH_SHORT).show();
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
        Intent intentToGallery = new Intent(Intent.ACTION_PICK);
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
                addProfilePic.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to image view
                addProfilePic.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}