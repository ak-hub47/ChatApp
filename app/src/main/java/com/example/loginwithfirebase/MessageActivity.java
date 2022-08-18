package com.example.loginwithfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.capybaralabs.swipetoreply.ISwipeControllerActions;
import com.capybaralabs.swipetoreply.SwipeController;
import com.example.loginwithfirebase.adapter.MessageAdapter;
import com.example.loginwithfirebase.model.Chat;
import com.example.loginwithfirebase.model.UserModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private TextView Username,entertext1,enterText2;
    DatabaseReference firebaseDatabase;
    FirebaseUser firebaseUser;
    ImageButton btnSend;
    EditText enterText;
    RecyclerView recyclerView1;
    MessageAdapter messageAdapter;
    DatabaseReference databaseReference;
    List<Chat> chatList;
    UserModel userModel;
    Chat chat;
     String UserId,Message;
     ValueEventListener valueEventListener;
     ImageView clickPicture;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Uri imageUri;
    StorageReference storageReference;
    StorageTask uploadTask;
    private int replychatposition=0;
    private String reply="no",chatPosition="",USERNAME="",chatPositionimage="";
    ImageView enterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        chatList=new ArrayList<>();
        btnSend=findViewById(R.id.btnSent);
        clickPicture=findViewById(R.id.clickPicture);
        enterText=findViewById(R.id.enterText);
        enterImage=findViewById(R.id.enterImage);
        final ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Username = findViewById(R.id.username);
        entertext1 = findViewById(R.id.entertext1);
        enterText2 = findViewById(R.id.entertext2);
        recyclerView1 = findViewById(R.id.messageRecyclerView);


        final Intent intent = getIntent();
        UserId = intent.getStringExtra("userId");

        clickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
            }
        });

        SwipeController controller = new SwipeController(MessageActivity.this, new ISwipeControllerActions() {
            @Override
            public void onSwipePerformed(final int position) {
                // Here you can handle the swipe-to-reply event
                entertext1.setVisibility(View.VISIBLE);
                replychatposition =  position;

                Log.e("SwipePosition", String.valueOf(replychatposition));
                USERNAME=userModel.getUsername();

                entertext1.setText(USERNAME);

                Chat chat=chatList.get(position);
                if(chat.getClickImage().equals("default")){
                    enterText2.setVisibility(View.VISIBLE);
                    chatPosition=chat.getMessage();
                    enterImage.setVisibility(View.GONE);
                    enterText2.setText(chatPosition);}
                else {

                    chatPositionimage=chat.getClickImage();
                    enterText2.setVisibility(View.GONE);
                    enterImage.setVisibility(View.VISIBLE);
                    Picasso.with(MessageActivity.this).load(chatPositionimage).into(enterImage);
                }





            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(controller);
        itemTouchHelper.attachToRecyclerView(recyclerView1);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users").child(UserId);
        storageReference= FirebaseStorage.getInstance().getReference("uploads");

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModel = dataSnapshot.getValue(UserModel.class);

                //Username.setText(userModel.getUsername());
                actionBar.setTitle(userModel.getUsername());

                readMessage(firebaseUser.getUid(),UserId);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(UserId);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message=enterText.getText().toString().trim();
                if(!Message.equals("")){

                    if(entertext1.getVisibility() == View.VISIBLE){
                        reply="yes";
                        entertext1.setVisibility(View.GONE);
                        enterText2.setVisibility(View.GONE);
                        enterImage.setVisibility(View.GONE);
                        sendMessage(firebaseUser.getUid(), UserId,Message);
                    }else {
                        reply="no" ;
                        sendMessage(firebaseUser.getUid(), UserId,Message);}


                }
                else {
                    Toast.makeText(MessageActivity.this,"Can't send null message",Toast.LENGTH_LONG).show();
                }
                enterText.setText("");
            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MessageActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView1.setLayoutManager(linearLayoutManager);
        messageAdapter=new MessageAdapter(MessageActivity.this,chatList);
        recyclerView1.setAdapter(messageAdapter);

        recyclerView1.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(MessageActivity.this,"Clicked",Toast.LENGTH_LONG).show();
                        //recyclerView1.setVerticalScrollbarPosition(position);
                      messageAdapter.itemclicked(chatList.get(position).getReplyposition());
                        recyclerView1.scrollToPosition(chatList.get(position).getReplyposition());
                        Log.e("scrollToPosition", String.valueOf(replychatposition+3));
                    }
                }));




    }

    private void seenMessage(final String userID){

        databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        valueEventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid())  && chat.getSender().equals(userID)){

                        HashMap<String,Object>  hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();

        Map hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen", false);
        hashMap.put("clickImage", "default");
        hashMap.put("reply", reply);
        hashMap.put("username", USERNAME);
        hashMap.put("chatposition", chatPosition);
        hashMap.put("chatpositionimage", chatPositionimage);
        hashMap.put("replyposition", replychatposition);

        String id=firebaseUser.getUid()+"__"+UserId;

        databaseReference.child("Chats").push().setValue(hashMap);


        chatlist();


    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void readMessage(final String myId, final String userId){


        databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    chat=snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                    chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){

                        chatList.add(chat);
                    }

                }
                messageAdapter.notifyDataSetChanged();
                recyclerView1.scrollToPosition(chatList.size()-1);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void clickImage(){

        final ProgressDialog progressDialognew = new ProgressDialog(MessageActivity.this);
        progressDialognew.setMessage("Loading Data...");
        progressDialognew.show();

        if(imageUri != null){
            storageReference= FirebaseStorage.getInstance().getReference("uploads");
            final StorageReference fileReference=storageReference.child(System.currentTimeMillis() +"."+ getFileExtension(imageUri));


            uploadTask=fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>> (){
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        String mUri=downloadUri.toString();

                        databaseReference=FirebaseDatabase.getInstance().getReference();
                        Map hashMap=new HashMap<>();
                        hashMap.put("sender",firebaseUser.getUid());
                        hashMap.put("receiver", UserId);
                        hashMap.put("message","default");
                        hashMap.put("isseen", false);
                        hashMap.put("clickImage", mUri);
                        hashMap.put("reply", reply);
                        hashMap.put("username", USERNAME);
                        hashMap.put("chatposition", chatPosition);
                        hashMap.put("chatpositionimage", chatPositionimage);
                        databaseReference.child("Chats").push().setValue(hashMap);

                        progressDialognew.dismiss();

                        chatlist();

                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.e("error",e.getMessage());

                }
            });
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            imageUri=data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){

            }else
                {

                    if(entertext1.getVisibility() == View.VISIBLE){
                        reply="yes";
                        entertext1.setVisibility(View.GONE);
                        enterText2.setVisibility(View.GONE);
                        enterImage.setVisibility(View.GONE);
                        clickImage();
                    }else {
                        reply="no" ;
                        clickImage();
                        USERNAME="";
                        chatPosition="";
                        chatPositionimage="";
                        }
                }






        }
    }
    private void chatlist() {
        final DatabaseReference chatReference=FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid()).child(UserId);

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatReference.child("id").setValue(UserId);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status){
        databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        databaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
        databaseReference.removeEventListener(valueEventListener);
    }
}
