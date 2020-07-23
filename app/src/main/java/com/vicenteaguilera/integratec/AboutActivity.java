package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle(R.string.acerca_de);
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/about.html");
        webView.setWebViewClient( new WebViewClient());
    }
}