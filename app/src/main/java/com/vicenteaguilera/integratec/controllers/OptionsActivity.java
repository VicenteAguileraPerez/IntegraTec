package com.vicenteaguilera.integratec.controllers;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.controllers.fragments.LoginFragment;
import com.vicenteaguilera.integratec.controllers.fragments.SignInFragment;

import java.util.Objects;


public class OptionsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Objects.requireNonNull(getSupportActionBar()).hide();

    }

}
