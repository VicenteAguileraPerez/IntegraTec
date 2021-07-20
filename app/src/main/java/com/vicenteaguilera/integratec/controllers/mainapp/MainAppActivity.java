package com.vicenteaguilera.integratec.controllers.mainapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.controllers.ListAdviserActivity;
import com.vicenteaguilera.integratec.controllers.OptionsActivity;
import com.vicenteaguilera.integratec.helpers.services.FirebaseAuthHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseFirestoreAsesorHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseFirestoreAsesoriaPublicaAsesoriaHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.ButtonHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.InternetHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;

import java.util.Objects;

public class MainAppActivity extends AppCompatActivity implements View.OnClickListener, Status {

    private final InternetHelper internetHelper = new InternetHelper();
    private MaterialButton button_asesores_disponibles,button_sesion_asesor;
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
        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               // status("Recibirás notificaciones cuando una asesoría sea creada");
               // new SendNotification().sendAllDevices("Vicente ","500 a 600", MainAppActivity.this);

            }
        });
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
                        new FirebaseFirestoreAsesorHelper().getData(FirebaseAuthHelper.getCurrentUser().getUid(),dialog,MainAppActivity.this,MainAppActivity.this);
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
