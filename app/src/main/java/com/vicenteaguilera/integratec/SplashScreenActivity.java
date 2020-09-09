package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vicenteaguilera.integratec.controllers.mainapp.MainAppActivity;
import com.vicenteaguilera.integratec.helpers.services.FirebaseAuthHelper;
import com.vicenteaguilera.integratec.helpers.services.FirestoreHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.InternetHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;

import java.sql.Time;
import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity  implements Status
{
    private TextView textView_IntegraTec;
    private ImageView imageView_Splash_Screen;
    private final FirestoreHelper firestoreHelper = new FirestoreHelper();
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

        imageView_Splash_Screen = findViewById(R.id.imageView_Splash_Screen);
        Log.e("time", new Time(System.currentTimeMillis()-SystemClock.elapsedRealtime()).toString());
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
        },timeSplash*2);

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
}
