package com.vicenteaguilera.integratec.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vicenteaguilera.integratec.R;


public class OptionsActivity extends AppCompatActivity implements  View.OnClickListener{

    private TextView button_Registrarse, button_IniciarSesion;
    private Fragment login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        button_Registrarse = findViewById(R.id.button_Registrarse);
        button_IniciarSesion = findViewById(R.id.button_IniciarSesion);

        button_Registrarse.setOnClickListener(this);
        button_IniciarSesion.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id)
        {

            case R.id.button_Registrarse:
                Toast.makeText(this, "Se dio clic en Registrarse", Toast.LENGTH_SHORT).show();
                //Snackbar.make(Objects.requireNonNull(getCurrentFocus()), "Se dio clic en Registrarse.", Snackbar.LENGTH_SHORT).show();
                break;

            case R.id.button_IniciarSesion:
                //login = new LoginFragment();
                //getSupportFragmentManager().beginTransaction().add(R.id.contenedor, login).commit();

                Toast.makeText(this, "Se dio clic en Iniciar Sesión", Toast.LENGTH_SHORT).show();
                //Snackbar.make(Objects.requireNonNull(getCurrentFocus()), "Se dio clic en Iniciar Sesión.", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }
}
