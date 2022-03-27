package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.messageapp.databinding.ActivitySigninBinding;
import com.example.messageapp.utilities.constants;
import com.example.messageapp.utilities.preferencemanager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class signin extends AppCompatActivity {
    private ActivitySigninBinding  binding;
    private preferencemanager preferencemanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencemanager = new preferencemanager(getApplicationContext());

        binding =ActivitySigninBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());
        Button signin=(Button) findViewById(R.id.buttonsignin);
//        signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i=new Intent(signin.this,signup.class);
//                startActivity(i);
//            }
//        });
        setListeners();

    }

    private void setListeners(){
        binding.textCreatenewAccount.setOnClickListener(v->
                startActivity(new Intent(getApplicationContext(),signup.class)));
        binding.buttonsignin.setOnClickListener(v -> {
            if(isValidSignInDetails()){
                signIn();
            }
        });

//binding.buttonsignin.setOnClickListener(v->addDatatoFirestore());

    }

    //    private void addDatatoFirestore(){
//        FirebaseFirestore database=FirebaseFirestore.getInstance();
//        HashMap<String,Object> data=new HashMap<>();
//        data.put("first_name","Janvi");
//        data.put("last_name","Virani");
//        database.collection("users")
//                .add(data)
//                .addOnSuccessListener(documentReference -> {
//                    Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
//                })
//               .addOnFailureListener(e -> {
//
//                   Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//
//        });
    private void signIn(){
        loading(true);
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        database.collection(constants.KEY_COLLECTION_USERS)
                .whereEqualTo(constants.KEY_EMAIL,binding.inputEmail.getText().toString())
                .whereEqualTo(constants.KEY_PASSWORD,binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
                        DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                        preferencemanager.putBoolean(constants.KEY_IS_SIGNED_IN,true);
                        preferencemanager.putString(constants.KEY_USER_ID,documentSnapshot.getId());
                        preferencemanager.putString(constants.KEY_NAME,documentSnapshot.getString(constants.KEY_NAME));
                        preferencemanager.putString(constants.KEY_IMAGE,documentSnapshot.getString(constants.KEY_IMAGE));
                        Intent intent=new Intent(getApplicationContext(),MainActivity3.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        showToast("successfully logged");
                    }else{
                        loading(false);
                        showToast("unable to sign in");
                    }
                });
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.buttonsignin.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonsignin.setVisibility(View.VISIBLE);
        }
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private Boolean isValidSignInDetails(){
        if(binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("enter email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("enter valid email");
            return false;
        }
        else if(binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("enter password");
            return false;
        }else{
            return true;
        }
    }
}