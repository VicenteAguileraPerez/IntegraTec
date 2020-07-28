package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class MainAppActivity extends AppCompatActivity implements View.OnClickListener {

    CardView button_asesores_disponibles,button_sesion_asesor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        Objects.requireNonNull(getSupportActionBar()).hide();

        button_asesores_disponibles = findViewById(R.id.button_asesores_disponibles);
        button_sesion_asesor = findViewById(R.id.button_sesion_asesores);
        button_sesion_asesor.setOnClickListener(this);
        button_asesores_disponibles.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        int id= view.getId();
        switch (id)
        {
            case R.id.button_asesores_disponibles:
                Snackbar.make(view,"Asesores Disponibles",Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.button_sesion_asesores:
                Snackbar.make(view,"Sesión Asesores",Snackbar.LENGTH_SHORT).show();
                break;

        }

    }
}
