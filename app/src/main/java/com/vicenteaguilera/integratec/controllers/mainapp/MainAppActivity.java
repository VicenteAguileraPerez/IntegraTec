package com.vicenteaguilera.integratec.controllers.mainapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
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
import com.vicenteaguilera.integratec.helpers.services.FirestoreHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.ButtonHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.InternetHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;

import java.util.Objects;

public class MainAppActivity extends AppCompatActivity implements View.OnClickListener, Status {

    private final InternetHelper internetHelper = new InternetHelper();
    private CardView button_asesores_disponibles,button_sesion_asesor;
    private WifiReceiver wifiReceiver = new WifiReceiver();
    private ButtonHelper buttonHelper = new ButtonHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        Objects.requireNonNull(getSupportActionBar()).hide();

        button_asesores_disponibles = findViewById(R.id.button_asesores_disponibles);
        button_sesion_asesor = findViewById(R.id.button_sesion_asesores);
        button_sesion_asesor.setOnClickListener(this);
        button_asesores_disponibles.setOnClickListener(this);
        buttonHelper.actionClickButton(button_sesion_asesor, getResources().getColor(R.color.color_blue1_light), getResources().getColor(R.color.color_blue1));
        buttonHelper.actionClickButton(button_asesores_disponibles, getResources().getColor(R.color.color_blue1_light), getResources().getColor(R.color.color_blue1));
    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wifiReceiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiReceiver);
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

                    intent = new Intent(MainAppActivity.this, ListAdviserActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_sesion_asesores:
                    if(FirebaseAuthHelper.getCurrentUser()!=null)
                    {
                        ProgressDialog dialog = ProgressDialog.show(MainAppActivity.this, "",
                                "Ingresando... ", true);
                        dialog.show();
                        new FirestoreHelper().getData(FirebaseAuthHelper.getCurrentUser().getUid(),dialog,MainAppActivity.this,MainAppActivity.this);
                    }
                    else
                    {
                        intent = new Intent(MainAppActivity.this, OptionsActivity.class);
                        startActivity(intent);
                    }
                    break;

            }
        }
        else
        {

            Toast.makeText(MainAppActivity.this,"Compruebe su conexión de Internet, para poder realizar acciones..",Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    public void status(String message)
    {
        Toast.makeText(MainAppActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
