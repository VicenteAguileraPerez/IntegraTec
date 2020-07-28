package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class ComplaintSuggestionsActivity extends AppCompatActivity implements View.OnClickListener {
    CardView cardView_ButtonPublicar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_suggestions);

        cardView_ButtonPublicar = findViewById(R.id.cardView_ButtonPublicar);
        cardView_ButtonPublicar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.cardView_ButtonPublicar:
                Snackbar.make(view,"¡Gracias, atenderemos pronto tu queja!",Snackbar.LENGTH_SHORT).show();
                break;
        }
    }
}