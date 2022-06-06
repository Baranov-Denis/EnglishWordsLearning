package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().hasExtra("LOGOUT")){
        if (getIntent().getStringExtra("LOGOUT").equals("1")) {
            finish();
        } }else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }
    }
}