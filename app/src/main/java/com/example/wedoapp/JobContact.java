package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobContact extends AppCompatActivity {

    private ImageButton ChatBackToJobList, JobContactBtn_sentMsg, JobContact_btn_addImage;
    private RecyclerView recyclerViewJobContact;
    private TextView JobContact_userName, JobContact_receiverID, JobContact_userStatus;
    private EditText JobContact_textMessage;
    private ImageView JobContact_userImage;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private ArrayList<Model_Chat> ChatConversationList;
    private AdapterChatConversation adapterChatConversation;

    private String projectID, userID, ReceiverName, Message, ReceiverImage;

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

    NotificationAPIService apiService;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_contact);

        JobContact_userStatus = findViewById(R.id.JobContact_userStatus);
        JobContact_userImage = findViewById(R.id.JobContact_userImage);
        JobContact_btn_addImage = findViewById(R.id.JobContact_btn_addImage);
        JobContact_receiverID = findViewById(R.id.JobContact_receiverID);
        JobContact_textMessage = findViewById(R.id.JobContact_textMessage);
        JobContact_userName = findViewById(R.id.JobContact_userName);
        ChatBackToJobList = findViewById(R.id.ChatBackToJobList);
        JobContactBtn_sentMsg = findViewById(R.id.JobContactBtn_sentMsg);
        recyclerViewJobContact = findViewById(R.id.recyclerViewJobContact);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        projectID = getIntent().getStringExtra("ProjectID");
        userID = getIntent().getStringExtra("UID");

        //init permission array
        cameraPermission =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission =  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        loadUser();
        readMessage();

        //layout for recycle view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        //set recycleView layout
        recyclerViewJobContact.setLayoutManager(linearLayoutManager);

        apiService = NotificationClient.getClient("https://fcm.googleapis.com/").create(NotificationAPIService.class);

        ChatBackToJobList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        JobContact_btn_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });

        JobContactBtn_sentMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                inputMessage();

            }
        });

    }

    private void loadUser() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Project");
        databaseReference.child(projectID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userID = ""+snapshot.child("UID").getValue();
                JobContact_receiverID.setText(userID);

                JobContact_receiverID.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference nameReference = FirebaseDatabase.getInstance().getReference("Users");
        nameReference.orderByChild("UID").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()) {

                    ReceiverImage = ""+ds.child("ProfileImage").getValue();
                    ReceiverName = ""+ds.child("FullName").getValue();
                    JobContact_userName.setText(ReceiverName);

                    String OnlineStatus = ""+ds.child("OnlineStatus").getValue();

                    /*if(OnlineStatus.equals("Online")){
                        JobContact_userStatus.setText(OnlineStatus);
                    }

                    else{
                        //convert timestamp to proper format dd/MM/yyyy
                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        calendar.setTimeInMillis(Long.parseLong(OnlineStatus));
                        String dateFormat = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar).toString();
                        JobContact_userStatus.setText("Last Seen " + dateFormat);
                    }*/

                    JobContact_userStatus.setVisibility(View.GONE);

                    try {
                        Picasso.get().load(ReceiverImage).placeholder(R.drawable.profile).into(JobContact_userImage);
                    }
                    catch (Exception e) {
                        JobContact_userImage.setImageResource(R.drawable.profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void inputMessage() {

        Message = JobContact_textMessage.getText().toString().trim();

        if (Message.equals("")){
            Toast.makeText(getApplicationContext(),"You can't send empty message.",Toast.LENGTH_SHORT).show();
            return;
        }

        saveMessage();

    }

    private void saveMessage() {
        final String timestamp = "" + System.currentTimeMillis();

        //add project data to database
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("MID", "" + timestamp);
        hashMap.put("ReceiverID", "" + userID);
        hashMap.put("ReceiverName", "" + ReceiverName);
        hashMap.put("Message", "" + Message);
        hashMap.put("SenderID", "" + auth.getUid());
        hashMap.put("MessageType", "Text");

        //save to database

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                JobContact_textMessage.setText("");
                Toast.makeText(JobContact.this,"Message sent.",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("ChatList").child(auth.getUid()).child(userID);
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatReference.child("ReceiverID").setValue(userID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        DatabaseReference chatReference2 = FirebaseDatabase.getInstance().getReference("ChatList").child(userID).child(auth.getUid());
        chatReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatReference2.child("ReceiverID").setValue(auth.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        final String msg = Message;
        DatabaseReference rfNotification = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
        rfNotification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Model_User model_user = snapshot.getValue(Model_User.class);
                if(notify){
                    sendNotification(userID, model_user.getFullName(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendNotification(String userID, String fullName, String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NotificationToken token = dataSnapshot.getValue(NotificationToken.class);
                    NotificationData data = new NotificationData(auth.getUid(), R.mipmap.ic_launcher, fullName+": "+message, "New Message",
                            userID);
                    NotificationSender sender = new NotificationSender(data, token.getToken());

                    apiService.sendNotification(sender)

                            .enqueue(new Callback<NotificationMyResponse>() {
                                @Override
                                public void onResponse(Call<NotificationMyResponse> call, Response<NotificationMyResponse> response) {
                                    if (response.code() == 200)
                                    {
                                        if (response.body().success != 1)
                                        {
                                            Toast.makeText(JobContact.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<NotificationMyResponse> call, Throwable t) {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void readMessage() {
        ChatConversationList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatConversationList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Chat model_chat = ds.getValue(Model_Chat.class);

                    if (model_chat.getReceiverID().equals(auth.getUid()) && model_chat.getSenderID().equals(userID)||model_chat.getReceiverID().equals(userID) && model_chat.getSenderID().equals(auth.getUid())){
                        ChatConversationList.add(model_chat);
                    }

                }
                //setup adapter
                adapterChatConversation = new AdapterChatConversation(JobContact.this, ChatConversationList);
                adapterChatConversation.notifyDataSetChanged();
                //set adapter
                recyclerViewJobContact.setAdapter(adapterChatConversation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendImageMessage(Uri image_uri) {
        //progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Image...");
        progressDialog.show();

        final String timestamp = "" + System.currentTimeMillis();
        String filePathAndName = "chat_images/" + "" + timestamp;

        //ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //get bitmap from image_uri
        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
        // bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        //  byte[] data = bao

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //image uploaded
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadImageUri = uriTask.getResult();

                if (uriTask.isSuccessful()){

                    //add image to database
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("MID", "" + timestamp);
                    hashMap.put("ReceiverID", "" + userID);
                    hashMap.put("ReceiverName", "" + ReceiverName);
                    hashMap.put("Message", "" + downloadImageUri);
                    hashMap.put("SenderID", "" + auth.getUid());
                    hashMap.put("MessageType", "Image");

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
                    databaseReference.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            progressDialog.dismiss();
                            Toast.makeText(JobContact.this,"Message sent.",Toast.LENGTH_SHORT).show();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                        }
                    });

                    DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("ChatList").child(auth.getUid()).child(userID);
                    chatReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                chatReference.child("ReceiverID").setValue(userID);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                    DatabaseReference chatReference2 = FirebaseDatabase.getInstance().getReference("ChatList").child(userID).child(auth.getUid());
                    chatReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                chatReference2.child("ReceiverID").setValue(auth.getUid());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

  /*  @Override
    protected void onResume(){
        super.onResume();
        String OnlineStatus = "Online";
        DatabaseReference statusReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        statusReference.child("OnlineStatus").setValue(OnlineStatus);

    }

    @Override
    protected void onPause(){
        super.onPause();
        String timestamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference statusReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        statusReference.child("OnlineStatus").setValue(timestamp);

    }*/

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

                //use image uri to upload image to firebase storage
                sendImageMessage(image_uri);

                //set to image view
                //seller_serPhoto.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //use image uri to upload image to firebase storage
                sendImageMessage(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}