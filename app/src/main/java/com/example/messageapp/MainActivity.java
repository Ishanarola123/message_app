package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Button b1=(Button) findViewById(R.id.b1);
        VideoView v1=(VideoView) findViewById(R.id.videoView);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,signin.class);
                startActivity(i);
            }
        });
        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.messagevideo);
        v1.setVideoURI(uri);
        v1.start();
        v1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
    }

    @Override
    protected void onPostResume() {
        VideoView v1=(VideoView) findViewById(R.id.videoView);
        v1.resume();
        super.onPostResume();
    }

    @Override
    protected void onRestart() {
        VideoView v1=(VideoView) findViewById(R.id.videoView);
        v1.start();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        VideoView v1=(VideoView) findViewById(R.id.videoView);
        v1.suspend();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        VideoView v1=(VideoView) findViewById(R.id.videoView);
        v1.stopPlayback();
        super.onDestroy();

    }
}