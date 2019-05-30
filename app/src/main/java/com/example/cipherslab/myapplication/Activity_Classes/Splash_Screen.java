package com.example.cipherslab.myapplication.Activity_Classes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.cipherslab.myapplication.R;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
      Splash ();
       }
private void Splash(){
    int Splash_TimeOut = 3000;
    new Handler ().postDelayed(new Runnable() {
        @Override
        public void run() {
            Intent RegIntent = new Intent(Splash_Screen.this, MainActivity.class);
            startActivity(RegIntent);
            finish();
        }
    }, Splash_TimeOut);

}
}
