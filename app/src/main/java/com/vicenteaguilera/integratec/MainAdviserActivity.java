package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainAdviserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_adviser);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id){
            case R.id.item_AcercaDe:
                Toast.makeText(this, "Acerca de...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item_QuejasSugerencias:
                Toast.makeText(this, "Quejas y sugerencias...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item_CerrarSesion:
                Toast.makeText(this, "Cerrar sesión...", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
