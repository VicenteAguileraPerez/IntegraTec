package com.vicenteaguilera.integratec.controllers.mainapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.SplashScreenActivity;
import com.vicenteaguilera.integratec.controllers.ListAdviserActivity;
import com.vicenteaguilera.integratec.controllers.OptionsActivity;
import com.vicenteaguilera.integratec.helpers.services.FirebaseAuthHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.InternetHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;

import java.util.Objects;

public class MainAppActivity extends AppCompatActivity implements View.OnClickListener {

    private final InternetHelper internetHelper = new InternetHelper();
    private CardView button_asesores_disponibles,button_sesion_asesor;
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
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new WifiReceiver(),intentFilter);
    }
    @Override
    public void onClick(View view)
    {
        if(internetHelper.isNetDisponible(MainAppActivity.this) && internetHelper.isOnlineNet())
        {

            Log.e("time", String.valueOf(internetHelper.timeAutomatically(MainAppActivity.this.getContentResolver())));
            int id= view.getId();
            Intent intent;
            switch (id)
            {
                case R.id.button_asesores_disponibles:
                    Snackbar.make(view,"Asesores Disponibles",Snackbar.LENGTH_SHORT).show();
                    intent = new Intent(MainAppActivity.this, ListAdviserActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_sesion_asesores:
                    Snackbar.make(view,"Sesión Asesores",Snackbar.LENGTH_SHORT).show();
                    intent = new Intent(MainAppActivity.this, OptionsActivity.class);
                    startActivity(intent);
                    break;

            }
        }
        else
        {

            Toast.makeText(MainAppActivity.this,"Compruebe su conexión de Internet, para poder realizar acciones..",Toast.LENGTH_SHORT).show();
        }


    }
}
