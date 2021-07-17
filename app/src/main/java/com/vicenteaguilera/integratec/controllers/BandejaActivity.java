
package com.vicenteaguilera.integratec.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.helpers.services.FirebaseAsesorHelper;
import com.vicenteaguilera.integratec.helpers.services.FirestoreHelper;
import com.vicenteaguilera.integratec.helpers.services.SendNotification;
import com.vicenteaguilera.integratec.helpers.utility.helpers.StaticHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesores;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.RealtimeAsesoria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BandejaActivity extends AppCompatActivity implements Status, ListaAsesores {

    private TextInputLayout editText_mensaje;
    private TextInputLayout editText_nombre;
    private TextInputLayout editText_mensaje_recibido;
    private TextInputLayout spinner_send_to;
    private MaterialButton cardView_ButtonEnviar;
    private WifiReceiver wifiReceiver = new WifiReceiver();
    private String titulo,mensaje;
    private final FirebaseAsesorHelper firebaseAsesorHelper = new FirebaseAsesorHelper();
    ArrayList<String> llaves = new ArrayList<>();
    ArrayList<String> valores = new ArrayList<>();
    boolean hayDatos;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wifiReceiver,intentFilter);
        ProgressDialog dialog = ProgressDialog.show(this, "", "buscando contactos...", true);
        dialog.show();
        firebaseAsesorHelper.getAllAsesores(this,dialog,this,this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            titulo = getIntent().getExtras().getString("titulo","nulo");
            Log.e("err",titulo+" el titulo");
            mensaje = getIntent().getExtras().getString("mensaje","nulo");
            Log.e("err",mensaje+" el mensaje");

            if(!titulo.equals("nulo"))
                cargarMensaje();
        }
        catch (NullPointerException e)
        {
            hayDatos = false;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiReceiver);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bandeja);
        setTitle("Bandeja de mensajes");
        editText_mensaje = findViewById(R.id.editText_mensaje);
        editText_mensaje_recibido = findViewById(R.id.editText_mensaje_recibido);
        editText_nombre = findViewById(R.id.editText_nombre);
        spinner_send_to = findViewById(R.id.spinner_send_to);
        cardView_ButtonEnviar = findViewById(R.id.cardView_ButtonEnviar);
        cardView_ButtonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String mensaje = editText_mensaje.getEditText().getText().toString();
                if(mensaje.length()!=0)
                {
                    if(mensaje.length()<=200)
                    {

                        if(Objects.requireNonNull(spinner_send_to.getEditText()).getText().length() != 0)
                        {
                            String from  = FirestoreHelper.asesor.getNombre()+" "+FirestoreHelper.asesor.getApellidos();
                            int id = valores.indexOf(spinner_send_to.getEditText().getText().toString());
                            String topic = llaves.get(id);
                            if(topic.equals("asesores"))
                            {
                                new SendNotification().sendAllAsesores("asesores,"+from,"Aviso para todos los asesores", editText_mensaje.getEditText().getText().toString(),BandejaActivity.this);
                            }
                            else
                            {
                                new SendNotification().sendAllAsesores(topic+","+from, "Te envió un mensaje", editText_mensaje.getEditText().getText().toString(),BandejaActivity.this);

                            }
                            status("Mensaje enviado");
                            onBackPressed();
                        }
                        else
                        {
                            spinner_send_to.setError("Debes seleccionar una carrera");
                            status("Debes seleccionar una carrera.");
                        }
                    }
                    else
                    {
                        editText_mensaje.setError("El Mensaje  debe ser menor a 200 caracteres");
                    }
                }
                else
                {
                    editText_mensaje.setError("Debe de escribir un mensaje");
                }
            }
        });


    }

    private void cargarMensaje()
    {
        if(!mensaje.equals(null) && !titulo.equals(null))
        {
            editText_nombre.getEditText().setText(titulo);
            editText_mensaje_recibido.getEditText().setText(mensaje);
        }
    }

    @Override
    public void status(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }



    @Override
    public void getAsesoresAll(Map<String, String> asesores)
    {
        llaves.clear();
        valores.clear();
        llaves.add("asesores");
        valores.add("Enviar a todos");
        llaves.addAll(asesores.keySet());
        valores.addAll(asesores.values());
        ArrayAdapter<String> arrayAdapter_Carreras= new ArrayAdapter<>(this, R.layout.custom_spinner_item, valores);
        ((AutoCompleteTextView)spinner_send_to.getEditText()).setAdapter(arrayAdapter_Carreras);

    }
    @Override
    public void getAsesoresRealtime(List<RealtimeAsesoria> realtimeAsesoriaList) {

    }
}