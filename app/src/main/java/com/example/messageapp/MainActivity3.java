package com.example.messageapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.messageapp.databinding.ActivityMain3Binding;
import com.example.messageapp.utilities.constants;
import com.example.messageapp.utilities.preferencemanager;

public class MainActivity3 extends AppCompatActivity {
    private ActivityMain3Binding binding;
    private preferencemanager  preferencemanager;
    private int msg_code=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(binding.getRoot());
//        preferencemanager=new preferencemanager(getApplicationContext());
        setContentView(R.layout.activity_main3);
        EditText mobileno,msg;
        mobileno=findViewById(R.id.mobileno);
        msg=findViewById(R.id.msg);
        Button btn=findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity3.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(mobileno.getText().toString(), null, msg.getText().toString(), null, null);
//
                        Toast.makeText(getApplicationContext(), "Message send successfully", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "SMS failed", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    requestmessagepermission();
                }
            };

//          loaduserdetails();



                               });
//private void loaduserdetails(){
//        binding.textname.setText(preferencemanager.getString(constants.KEY_NAME));
//        byte[] bytes= Base64.decode(preferencemanager.getString(constants.KEY_IMAGE),Base64.DEFAULT);
//    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//    binding.imageprofile.setImageBitmap(bitmap);

}

    private void requestmessagepermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)){
            new AlertDialog.Builder(this)
                    .setTitle("permission needed");
        }else{
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.SEND_SMS},msg_code);
        }
    }
    }

