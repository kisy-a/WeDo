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
import android.widget.Button;
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

public class ChatInbox extends AppCompatActivity {

    private ImageButton ChatHisBackToMainList, chatBtn_sentMsg, inbox_btn_addImage;
    private RecyclerView recyclerViewChatConversation;
    private TextView inboxMsg_userName, inboxMsg_receiverID, inboxMsg_userStatus;
    private EditText inbox_textMessage;
    private ImageView inboxMsg_userImage;
    Button btn_userReport;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private ArrayList<Model_Chat> ChatConversationList;
    private AdapterChatConversation adapterChatConversation;

    private String ReceiverID, ReceiverName, Message, ReceiverImage, SenderID, MessageImage;

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
        setContentView(R.layout.activity_chat_inbox);

        inboxMsg_userStatus = findViewById(R.id.inboxMsg_userStatus);
        inboxMsg_userImage = findViewById(R.id.inboxMsg_userImage);
        inbox_btn_addImage = findViewById(R.id.inbox_btn_addImage);
        inboxMsg_receiverID = findViewById(R.id.inboxMsg_receiverID);
        inbox_textMessage = findViewById(R.id.inbox_textMessage);
        inboxMsg_userName = findViewById(R.id.inboxMsg_userName);
        ChatHisBackToMainList = findViewById(R.id.ChatHisBackToMainList);
        chatBtn_sentMsg = findViewById(R.id.chatBtn_sentMsg);
        recyclerViewChatConversation = findViewById(R.id.recyclerViewChatConversation);
        btn_userReport = findViewById(R.id.btn_userReport);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ReceiverID = getIntent().getStringExtra("ReceiverID");

        //init permission array
        cameraPermission =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission =  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        loadUser();
        readMessage();

        //layout for recycleView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        //set recycleView layout
        recyclerViewChatConversation.setLayoutManager(linearLayoutManager);

        apiService = NotificationClient.getClient("https://fcm.googleapis.com/").create(NotificationAPIService.class);

        ChatHisBackToMainList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        chatBtn_sentMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                inputMessage();

            }
        });

        inbox_btn_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });

        btn_userReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReportUser.class);
                intent.putExtra("ReceiverID", ReceiverID);
                startActivity(intent);
            }
        });

    }
    private void loadUser() {

        DatabaseReference nameReference = FirebaseDatabase.getInstance().getReference("Users");
        nameReference.orderByChild("UID").equalTo(ReceiverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()) {

                    ReceiverImage = ""+ds.child("ProfileImage").getValue();
                    ReceiverID = ""+ds.child("UID").getValue();
                    ReceiverName = ""+ds.child("FullName").getValue();
                    String OnlineStatus = ""+ds.child("OnlineStatus").getValue();

                    /*if(OnlineStatus.equals("Online")){
                        inboxMsg_userStatus.setText(OnlineStatus);
                    }

                    else{
                        //convert timestamp to proper format dd/MM/yyyy
                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        calendar.setTimeInMillis(Long.parseLong(OnlineStatus));
                        String dateFormat = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar).toString();
                        inboxMsg_userStatus.setText("Last Seen " + dateFormat);
                    }*/

                    inboxMsg_userName.setText(ReceiverName);
                    inboxMsg_receiverID.setText(ReceiverID);

                    inboxMsg_userStatus.setVisibility(View.GONE);
                    inboxMsg_receiverID.setVisibility(View.INVISIBLE);


                    try {
                        Picasso.get().load(ReceiverImage).placeholder(R.drawable.profile).into(inboxMsg_userImage);
                    }
                    catch (Exception e) {
                        inboxMsg_userImage.setImageResource(R.drawable.profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void inputMessage() {

        Message = inbox_textMessage.getText().toString().trim();

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
        hashMap.put("ReceiverID", "" + ReceiverID);
        hashMap.put("ReceiverName", "" + ReceiverName);
        hashMap.put("Message", "" + Message);
        hashMap.put("SenderID", "" + auth.getUid());
        hashMap.put("MessageType", "Text");


        //save to database

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                inbox_textMessage.setText("");
                Toast.makeText(ChatInbox.this,"Message sent.",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("ChatList").child(auth.getUid()).child(ReceiverID);
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatReference.child("ReceiverID").setValue(ReceiverID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        DatabaseReference chatReference2 = FirebaseDatabase.getInstance().getReference("ChatList").child(ReceiverID).child(auth.getUid());
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
                    sendNotification(ReceiverID, model_user.getFullName(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(String receiverID, String fullName, String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiverID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NotificationToken token = dataSnapshot.getValue(NotificationToken.class);
                    NotificationData data = new NotificationData(auth.getUid(), R.mipmap.ic_launcher, fullName+": "+message, "New Message",
                            ReceiverID);
                    NotificationSender sender = new NotificationSender(data, token.getToken());

                    apiService.sendNotification(sender)

                            .enqueue(new Callback<NotificationMyResponse>() {
                                @Override
                                public void onResponse(Call<NotificationMyResponse> call, Response<NotificationMyResponse> response) {
                                    if (response.code() == 200)
                                    {
                                        if (response.body().success != 1)
                                        {
                                            Toast.makeText(ChatInbox.this, "Failed", Toast.LENGTH_SHORT).show();
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

                    if (model_chat.getReceiverID().equals(auth.getUid()) && model_chat.getSenderID().equals(ReceiverID)||model_chat.getReceiverID().equals(ReceiverID) && model_chat.getSenderID().equals(auth.getUid())){
                        ChatConversationList.add(model_chat);
                    }

                }
                //setup adapter
                adapterChatConversation = new AdapterChatConversation(ChatInbox.this, ChatConversationList);
                adapterChatConversation.notifyDataSetChanged();
                //set adapter
                recyclerViewChatConversation.setAdapter(adapterChatConversation);
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
                    hashMap.put("ReceiverID", "" + ReceiverID);
                    hashMap.put("ReceiverName", "" + ReceiverName);
                    hashMap.put("Message", "" + downloadImageUri);
                    hashMap.put("SenderID", "" + auth.getUid());
                    hashMap.put("MessageType", "Image");

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
                    databaseReference.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            progressDialog.dismiss();
                            Toast.makeText(ChatInbox.this,"Message sent.",Toast.LENGTH_SHORT).show();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                        }
                    });

                    DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("ChatList").child(auth.getUid()).child(ReceiverID);
                    chatReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                chatReference.child("ReceiverID").setValue(ReceiverID);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                    DatabaseReference chatReference2 = FirebaseDatabase.getInstance().getReference("ChatList").child(ReceiverID).child(auth.getUid());
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