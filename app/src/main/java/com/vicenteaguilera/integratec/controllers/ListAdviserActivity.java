package com.vicenteaguilera.integratec.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vicenteaguilera.integratec.CreateCodeQRActivity;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.adapters.AsesoriaRealtimeAdapter;
import com.vicenteaguilera.integratec.helpers.services.FirestoreHelper;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesores;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.RealtimeAsesoria;

import java.util.List;

public class ListAdviserActivity extends AppCompatActivity implements ListaAsesores, Status {

    private ProgressBar progressBar;
    private ListView listView_asesores;
    private AsesoriaRealtimeAdapter asesoriaRealtimeAdapter;
    private TextView textView_no_asesores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_adviser);
        progressBar = findViewById(R.id.progressBar);
        textView_no_asesores = findViewById(R.id.textView_no_asesores);
        listView_asesores = findViewById(R.id.listview_asesores);


    }

    @Override
    protected void onStart()
    {
        new FirestoreHelper().listenAsesorias(this);
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.overflow, menu);
        menu.removeItem(R.id.item_CerrarSesion);
        menu.removeItem(R.id.item_EditarPerfil);
        menu.removeItem(R.id.item_Leer_QR);


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.item_AcercaDe:
                Toast.makeText(ListAdviserActivity.this, getResources().getText(R.string.acerca_de)+"...", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.item_QuejasSugerencias:
                Toast.makeText(ListAdviserActivity.this,getResources().getText(R.string.quejasSugerencias)+"...", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, ComplaintSuggestionsActivity.class);
                startActivity(intent);
                break;
            case R.id.item_ActualizarLista:
                listView_asesores.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                new FirestoreHelper().listenAsesorias(this);
                break;

            case R.id.item_Crear_QR:
                intent = new Intent(this, CreateCodeQRActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getAsesoresRealtime(List<RealtimeAsesoria> realtimeAsesoriaList)
    {
        progressBar.setVisibility(View.INVISIBLE);
        listView_asesores.setAdapter(null);
        if(realtimeAsesoriaList.size()==0)
        {
            textView_no_asesores.setVisibility(View.VISIBLE);
            listView_asesores.setVisibility(View.INVISIBLE);
            listView_asesores.setAdapter(null);

        }
        else {

            progressBar.setVisibility(View.INVISIBLE);
            listView_asesores.setVisibility(View.VISIBLE);
            textView_no_asesores.setVisibility(View.INVISIBLE);
            if(listView_asesores.getAdapter()!=null)
            {
                //se actualiza la lista y se notifica al adaptador
                asesoriaRealtimeAdapter.setAsesorias(realtimeAsesoriaList);
            }
            else
            {
                asesoriaRealtimeAdapter = new AsesoriaRealtimeAdapter(ListAdviserActivity.this, R.layout.list_item_asesoria, realtimeAsesoriaList);
                listView_asesores.setAdapter(asesoriaRealtimeAdapter);
            }



        }
    }

    @Override
    public void status(String message) {
        Toast.makeText(ListAdviserActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
