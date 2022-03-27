package com.example.messageapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.messageapp.databinding.ActivitySigninBinding;
import com.example.messageapp.databinding.ActivitySignupBinding;
import com.example.messageapp.utilities.constants;
import com.example.messageapp.utilities.preferencemanager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import kotlin.jvm.internal.PackageReference;

public class signup extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private String encodedImage;
    private preferencemanager preferencemanager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferencemanager=new preferencemanager(getApplicationContext());
        setListeners();
    }

    private void setListeners() {
        binding.textsignin.setOnClickListener(v -> onBackPressed());
        binding.buttonsignup.setOnClickListener(v -> {
            if (isvalidSignupDetails()){
                signup();
            }
        });
      binding.layoutImage.setOnClickListener(v ->{
          Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
          pickImage.launch(intent);


      });

    }

    private void showtoast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signup() {
        loading(true);
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        HashMap<String,Object> user=new HashMap<>();
        user.put(constants.KEY_NAME,binding.inputname.getText().toString());
        user.put(constants.KEY_EMAIL,binding.signupemail.getText().toString());
        user.put(constants.KEY_PASSWORD,binding.inputPassword.getText().toString());
        user.put(constants.KEY_IMAGE,encodedImage);
       database.collection(constants.KEY_COLLECTION_USERS)
               .add(user)
               .addOnSuccessListener(documentReference -> {
                loading(false);
                preferencemanager.putBoolean(constants.KEY_IS_SIGNED_IN,true);
                preferencemanager.putString(constants.KEY_USER_ID,documentReference.getId());
                preferencemanager.putString(constants.KEY_NAME,binding.inputname.getText().toString());
                preferencemanager.putString(constants.KEY_IMAGE,encodedImage);
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


               })
               .addOnFailureListener(e->{
                   loading(false);
                   showtoast(e.getMessage());

               });





    }

    private String encodededImage(Bitmap bitmap){
        int previewWidth=150;
        int previewHight=bitmap.getDensity() * previewWidth/bitmap.getWidth();
        Bitmap previewBitmap =Bitmap.createScaledBitmap(bitmap,previewWidth,previewHight,false);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);


    }

     private  final  ActivityResultLauncher<Intent> pickImage=registerForActivityResult(

             new ActivityResultContracts.StartActivityForResult(),
             result -> {
                 if (result.getResultCode() == RESULT_OK){
                     if (result.getData() != null){
                         Uri imageUri=result.getData().getData();
                         try {
                             InputStream inputStream= getContentResolver().openInputStream(imageUri);
                             Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                             binding.imageprofile.setImageBitmap(bitmap);
                             binding.textAddimaage.setVisibility(View.GONE);
                             encodedImage=encodededImage(bitmap);


                         }
                         catch (FileNotFoundException e){
                             e.printStackTrace();

                         }
                     }
                 }

             }
     );



    private Boolean isvalidSignupDetails() {
        if (encodedImage == null) {
            showtoast("select profile Image");
            return false;

        } else if (binding.inputname.getText().toString().trim().isEmpty()) {
            showtoast("enter name:");
            return false;
        } else if (binding.signupemail.getText().toString().trim().isEmpty()) {
            showtoast("enter email:");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.signupemail.getText().toString()).matches()) {
            showtoast("enter valid image");
            return false;
        }else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showtoast("enter password");
            return false;
        }
        else if (binding.confirmpassword.getText().toString().trim().isEmpty()){
            showtoast("Confirm your password");
            return false;
        }
        else if (!binding.inputPassword.getText().toString().equals(binding.confirmpassword.getText().toString())){
            showtoast("password & confirm password must be same");
            return false;
        }
        else {
            return true;
        }



    }

    private void loading(Boolean isloading){
        if (isloading){
            binding.buttonsignup.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);

        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonsignup.setVisibility(View.VISIBLE);
        }
    }
}
