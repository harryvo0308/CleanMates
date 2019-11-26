package com.example.harryvo.cleanermates;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.harryvo.cleanermates.Common.Common;
import com.example.harryvo.cleanermates.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference userDB;

    String newname, newaddress, newphone, newsuburb;

    Uri newpic;

    EditText User_edtname;

    CircleImageView User_edtpic;

    EditText User_edtaddress;

    EditText User_edtphone;

    TextView User_edtemail;


    EditText User_edtsuburb;


    Button User_btnsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Init Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        userDB = database.getReference("Users");


        User_edtaddress= (EditText)findViewById(R.id.user_editaddress);
        User_edtemail=(TextView) findViewById(R.id.user_editemail);
        User_edtname= (EditText) findViewById(R.id.user_editname);
        User_edtphone=(EditText) findViewById(R.id.user_editphone);
        User_edtsuburb= (EditText) findViewById(R.id.user_editsuburb);
        User_edtpic=(CircleImageView)findViewById(R.id.user_editpic);


        User_btnsave=(Button)findViewById(R.id.btnSave);


        //Set change profile picture
        User_edtpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        //Set button save

        User_btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        //Get profile
        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        Common.currentUser = ds.getValue(User.class);
                        String userName= ds.child("name").getValue().toString();
                        User_edtname.setText(userName);
                    }

                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        Common.currentUser = ds.getValue(User.class);
                        String userAddress= ds.child("address").getValue().toString();
                        User_edtaddress.setText(userAddress);

                    }

                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        Common.currentUser = ds.getValue(User.class);
                        String userSubrub= ds.child("suburb").getValue().toString();
                        User_edtsuburb.setText(userSubrub);
                    }

                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        Common.currentUser = ds.getValue(User.class);
                        String userPhone = ds.child("phone").getValue().toString();
                        User_edtphone.setText(userPhone);
                    }
                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        Common.currentUser = ds.getValue(User.class);
                        String userEmail = ds.child("email").getValue().toString();
                        User_edtemail.setText(userEmail);
                    }

                    Glide.clear(User_edtpic);
                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        Common.currentUser = ds.getValue(User.class);
                        String userPic = ds.child("userpic").getValue().toString();
                        switch (userPic) {
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(User_edtpic);
                                break;
                            default:
                                Glide.with(getApplication()).load(userPic).into(User_edtpic);
                                break;
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //Update information

    private void saveUserInformation(){

        newname = User_edtname.getText().toString();
        newaddress = User_edtaddress.getText().toString();
        newphone = User_edtphone.getText().toString();
        newsuburb = User_edtsuburb.getText().toString();

        Map NewUserInf = new HashMap();

        NewUserInf.put("name", newname);
        NewUserInf.put("address", newaddress);
        NewUserInf.put("phone", newphone);
        NewUserInf.put("suburb", newsuburb);

        userDB.child(mAuth.getCurrentUser().getUid()).updateChildren(NewUserInf);

        //Upload picture

        if(newpic !=null){
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("userpic");

            Bitmap bitmap = null;

            try{
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),newpic);
            }catch (IOException e){
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20,baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask= filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map UserInfo = new HashMap();
                            UserInfo.put("userpic",uri.toString());
                            userDB.child(mAuth.getCurrentUser().getUid()).updateChildren(UserInfo);

                            finish();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            finish();
                            return;
                        }
                    });
                }
            });
        }else{
            finish();
        }


    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data );
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            newpic = imageUri;
            User_edtpic.setImageURI(newpic);
        }
    }

}
