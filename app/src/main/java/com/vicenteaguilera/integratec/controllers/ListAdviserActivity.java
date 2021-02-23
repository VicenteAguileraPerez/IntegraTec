package com.vicenteaguilera.integratec.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
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
import com.vicenteaguilera.integratec.helpers.utility.helpers.AlertDialogPersonalized;
import com.vicenteaguilera.integratec.helpers.utility.helpers.InternetHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.PropiertiesHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesores;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.RealtimeAsesoria;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListAdviserActivity extends AppCompatActivity implements ListaAsesores, Status {

    private ProgressBar progressBar;
    private ListView listView_asesores;
    private AsesoriaRealtimeAdapter asesoriaRealtimeAdapter;
    private TextView textView_no_asesores;
    private WifiReceiver wifiReceiver = new WifiReceiver();
    private InternetHelper internetHelper = new InternetHelper();

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
        if(internetHelper.timeAutomatically(getContentResolver()))
        {
            int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            if(day >= Calendar.MONDAY && day <= Calendar.FRIDAY)
            {
                int hora = Integer.parseInt(PropiertiesHelper.getHora("HH:mm").substring(0, 2));
                if (hora > 7 && hora < 20) {
                    new FirestoreHelper().listenAsesorias(this);
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    textView_no_asesores.setVisibility(View.VISIBLE);
                    textView_no_asesores.setText("No hay asesores por ahora.");
                    new AlertDialogPersonalized().alertDialogInformacion("Las asesorías estarán disponibles entre 8:00 am y las 8:00 pm.", ListAdviserActivity.this);

                }
            }
            else
            {
                progressBar.setVisibility(View.INVISIBLE);
                textView_no_asesores.setVisibility(View.VISIBLE);
                textView_no_asesores.setText("No hay asesores por ahora.");
                new AlertDialogPersonalized().alertDialogInformacion("Las asesorías están disponibles de Lunes a Viernes", ListAdviserActivity.this);

            }
        }
        else
        {
            getAsesoresRealtime(new ArrayList<RealtimeAsesoria>());
            status("No podrás ver las asesorías disponibles hasta que actives la hora automática en tu dispositivo.");
        }

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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.overflow, menu);
        menu.removeItem(R.id.item_CerrarSesion);
        menu.removeItem(R.id.item_EditarPerfil);
        menu.removeItem(R.id.item_Crear_PDF_asesorias);
        menu.removeItem(R.id.item_Crear_PDF_asesorados);
        menu.removeItem(R.id.item_nuevo_semestre);
        menu.removeItem(R.id.item_Asesorados);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Calendar cldr = Calendar.getInstance();
        int id = item.getItemId();
        Intent intent;
        if(cldr.get(Calendar.DAY_OF_WEEK)>=Calendar.MONDAY && cldr.get(Calendar.DAY_OF_WEEK)<=Calendar.FRIDAY)
        {
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            Log.e("Hour: ", hour+"");
            if(hour>=8 && hour<20) {

                switch (id){
                    case R.id.item_AcercaDe:
                        Toast.makeText(ListAdviserActivity.this, getResources().getText(R.string.acerca_de)+"...", Toast.LENGTH_SHORT).show();
                        intent = new Intent(this, AboutActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_Horarios:

                        intent = new Intent(this, HorarioActivity.class);
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
            }else {

                switch (id) {
                    case R.id.item_AcercaDe:
                        Toast.makeText(ListAdviserActivity.this, getResources().getText(R.string.acerca_de) + "...", Toast.LENGTH_SHORT).show();
                        intent = new Intent(this, AboutActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_Horarios:

                        intent = new Intent(this, HorarioActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.item_QuejasSugerencias:
                        Toast.makeText(ListAdviserActivity.this, getResources().getText(R.string.quejasSugerencias) + "...", Toast.LENGTH_SHORT).show();
                        intent = new Intent(this, ComplaintSuggestionsActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.item_Crear_QR:
                        intent = new Intent(this, CreateCodeQRActivity.class);
                        startActivity(intent);
                        break;
                }
            }
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
