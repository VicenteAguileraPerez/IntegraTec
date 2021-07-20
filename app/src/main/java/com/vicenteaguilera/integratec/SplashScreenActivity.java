package com.vicenteaguilera.integratec;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Layout;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.vicenteaguilera.integratec.controllers.mainapp.MainAppActivity;
import com.vicenteaguilera.integratec.helpers.services.FirebaseAuthHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseFirestoreAsesorHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseFirestoreAsesoriaPublicaAsesoriaHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseFirestoreMensajesHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.InternetHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;

import java.sql.Time;
import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity  implements Status
{
    private ConstraintLayout constraintLayout;
    private TextView textView_IntegraTec,textView_datos;
    private ImageView imageView_splash_screen,imageView_splash_screen_2,imageView_splash_screen_3;
    private final FirebaseFirestoreAsesorHelper firestoreHelper = new FirebaseFirestoreAsesorHelper();
    private final InternetHelper internetHelper = new InternetHelper();
    private WifiReceiver wifiReceiver = new WifiReceiver();

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final int timeSplash = 1500;
        final Status status=this;
        Objects.requireNonNull(getSupportActionBar()).hide();
        textView_IntegraTec = findViewById(R.id.textView_IntegraTec);

        imageView_splash_screen = findViewById(R.id.imageView_Splash_Screen);
        imageView_splash_screen_2 = findViewById(R.id.imageView_splash_screen2);
        imageView_splash_screen_3 = findViewById(R.id.imageView_splash_screen3);
        textView_datos = findViewById(R.id.textView_datos);
        setInfo();
        constraintLayout = findViewById(R.id.activity);
        Log.e("time", new Time(System.currentTimeMillis()-SystemClock.elapsedRealtime()).toString());

        new Handler().postDelayed(new Runnable() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void run() {
                imageView_splash_screen.setImageResource(R.drawable.ic_amigos);
                constraintLayout.setBackground(getDrawable(R.color.colorAccent));
                textView_IntegraTec.setText(R.string.app_name);
                textView_datos.setVisibility(View.INVISIBLE);
                imageView_splash_screen_2.setVisibility(View.INVISIBLE);
                imageView_splash_screen_3.setVisibility(View.INVISIBLE);
                Intent intent = null;
                ProgressDialog dialog = ProgressDialog.show(SplashScreenActivity.this, "",
                        "Ingresando... ", true);
                dialog.show();
                if(internetHelper.isNetDisponible(SplashScreenActivity.this) && internetHelper.isOnlineNet())
                {
                    go(dialog,  SplashScreenActivity.this);

                }
                else
                {
                    setMain(dialog);
                    Toast.makeText(SplashScreenActivity.this,"Compruebe su conexión de Internet",Toast.LENGTH_SHORT).show();
                }
            }
        },timeSplash*3);




    }



    @Override
    public void status(String message)
    {
        Toast.makeText(SplashScreenActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    private void go(ProgressDialog dialog, Status status)
    {
        if(FirebaseAuthHelper.getCurrentUser()!=null)
        {
            firestoreHelper.getData(FirebaseAuthHelper.getCurrentUser().getUid(),dialog,status,SplashScreenActivity.this);
        }
        else {
           setMain(dialog);
        }
    }
    private void setMain(ProgressDialog dialog)
    {
        dialog.dismiss();
        Intent intent = new Intent(SplashScreenActivity.this, MainAppActivity.class);
        startActivity(intent);
        finish();
    }
    public void setInfo()
    {
        String titulo ="IntegraTec";
        String texto =" es una app desarrollada por alumnos y docentes de la carrera de Ingeniería en Sistemas Computacionales para el departamento " +
                "Ciencias Básicas del Instituto Tecnológico Superior de Uruapan.";
        textView_datos.setText(titulo +texto, TextView.BufferType.SPANNABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView_datos.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
        Spannable s = (Spannable)textView_datos.getText();
        int start = titulo.length();
        int end = start + texto.length();
        s.setSpan(new ForegroundColorSpan(0xFF0047BA), 0, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
