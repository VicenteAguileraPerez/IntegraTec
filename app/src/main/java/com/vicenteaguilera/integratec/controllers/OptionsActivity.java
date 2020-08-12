package com.vicenteaguilera.integratec.controllers;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.vicenteaguilera.integratec.R;

import java.util.Objects;


public class OptionsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Objects.requireNonNull(getSupportActionBar()).hide();

    }

}
