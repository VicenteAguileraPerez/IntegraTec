package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.vicenteaguilera.integratec.controllers.mainapp.MainAppActivity;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {
    private TextView textView_IntegraTec;
    private ImageView imageView_Splash_Screen;
    private final int timeSplash = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        textView_IntegraTec = findViewById(R.id.textView_IntegraTec);
        imageView_Splash_Screen = findViewById(R.id.imageView_Splash_Screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textView_IntegraTec.setText(R.string.app_name);
                imageView_Splash_Screen.setImageResource(R.mipmap.colaboracion);
            }
        },timeSplash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainAppActivity.class);
                startActivity(intent);
                finish();
            }
        },timeSplash*2);

    }
}