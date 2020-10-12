package com.vicenteaguilera.integratec.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;

public class HorarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
        setTitle(R.string.acerca_de);
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/horarios/index.html");
        webView.setWebViewClient( new WebViewClient());
    }
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

}