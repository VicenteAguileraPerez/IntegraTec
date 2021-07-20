
package com.vicenteaguilera.integratec.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.vicenteaguilera.integratec.AsesoradosActivity;
import com.vicenteaguilera.integratec.MensajesActivity;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.controllers.mainapp.MainAppActivity;
import com.vicenteaguilera.integratec.helpers.services.FirebaseFirestoreAsesorHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseFirestoreAsesoriaPublicaAsesoriaHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseFirestoreMensajesHelper;
import com.vicenteaguilera.integratec.helpers.services.SendNotification;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesores;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesorias;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.Mensaje;
import com.vicenteaguilera.integratec.models.RealtimeAsesoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BandejaActivity extends AppCompatActivity implements Status,ListaAsesores {

    private TextInputLayout editText_mensaje;
    private TextInputLayout editText_nombre;
    private TextInputLayout editText_mensaje_recibido;
    private TextInputLayout spinner_send_to;
    private MaterialButton cardView_ButtonEnviar,cardView_ButtonMensajes;
    private WifiReceiver wifiReceiver = new WifiReceiver();
    private String titulo,mensaje;
    private FirebaseFirestoreAsesorHelper firebaseFirestoreAsesorHelper = new FirebaseFirestoreAsesorHelper();
    private final FirebaseFirestoreMensajesHelper firebaseFirestoreAsesoriaPublicaAsesoriaHelper  = new FirebaseFirestoreMensajesHelper();
    private ArrayList<String> llaves = new ArrayList<>();
    private ArrayList<String> valores = new ArrayList<>();
    private boolean hayDatos;
    @Override
    protected void onStart()
    {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wifiReceiver,intentFilter);
        if(FirebaseFirestoreAsesorHelper.asesor == null)
        {
            //iniciar sesion
            cardView_ButtonEnviar.setText("Iniciar sesion para responder");
            editText_mensaje.setVisibility(View.GONE);
            spinner_send_to.setVisibility(View.GONE);
            cardView_ButtonMensajes.setVisibility(View.GONE);
        }
        else
        {
            //mostrar información
            ProgressDialog dialog = ProgressDialog.show(this, "", "buscando contactos...", true);
            dialog.show();
            firebaseFirestoreAsesorHelper.getAllAsesores(this,dialog,this);
            cardView_ButtonEnviar.setText("Enviar");
            editText_mensaje.setVisibility(View.VISIBLE);
            spinner_send_to.setVisibility(View.VISIBLE);
            cardView_ButtonMensajes.setVisibility(View.VISIBLE);
        }
    }
    public static ArrayList<Mensaje> myList = null;
    @Override
    protected void onResume() {
        super.onResume();

        if(BandejaActivity.myList.size()==0)
        {
            cardView_ButtonMensajes.setVisibility(View.GONE);

        }
        else
        {
            cardView_ButtonMensajes.setVisibility(View.VISIBLE);
        }
        try
        {
            myList = (ArrayList<Mensaje>) getIntent().getSerializableExtra("mensajes");
            titulo = getIntent().getExtras().getString("titulo","nulo");
            Log.e("err",titulo+" el titulo");
            mensaje = getIntent().getExtras().getString("mensaje","nulo");
            Log.e("err",mensaje+" el mensaje");
            int pos = MensajesActivity.pos;
            Log.e("err",pos+" pos");
            if(!titulo.equals("nulo"))
                cargarMensaje();
            else if(pos>=0)
            {
                titulo = BandejaActivity.myList.get(pos).getFrom();
                mensaje ="Enviado a las "+BandejaActivity.myList.get(pos).getFecha()+"\n"+BandejaActivity.myList.get(pos).getMensaje();
                MensajesActivity.pos=-1;
                cargarMensaje();
            }

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
        myList = (ArrayList<Mensaje>) getIntent().getSerializableExtra("mensajes");
        cardView_ButtonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(cardView_ButtonEnviar.getText().toString().equals("Enviar"))
                {
                    String mensaje = editText_mensaje.getEditText().getText().toString();
                    if (mensaje.length() != 0) {
                        if (mensaje.length() <= 200) {

                            if (Objects.requireNonNull(spinner_send_to.getEditText()).getText().length() != 0) {
                                String from  = FirebaseFirestoreAsesorHelper.asesor.getNombre()+" "+ FirebaseFirestoreAsesorHelper.asesor.getApellidos();
                                int id = valores.indexOf(spinner_send_to.getEditText().getText().toString());
                                String mensajeEnviar = editText_mensaje.getEditText().getText().toString();
                                String topic = llaves.get(id);
                                if (topic.equals("asesores")) {

                                     new SendNotification().sendAllAsesores("asesores,"+from,"Aviso para todos los asesores", editText_mensaje.getEditText().getText().toString(),BandejaActivity.this);
                                } else {
                                    ProgressDialog dialog = ProgressDialog.show(BandejaActivity.this, "Enviar mensaje", "enviando mensaje...", true);
                                    dialog.show();
                                    firebaseFirestoreAsesoriaPublicaAsesoriaHelper.mensajeAsesor(topic, mensajeEnviar, BandejaActivity.this, dialog, BandejaActivity.this);
                                    editText_nombre.getEditText().setText("");
                                    editText_mensaje.getEditText().setText("");

                                }
                                status("Mensaje enviado");

                            } else {
                                spinner_send_to.setError("Debes seleccionar una carrera");
                                status("Debes seleccionar una carrera.");
                            }
                        } else {
                            editText_mensaje.setError("El Mensaje  debe ser menor a 200 caracteres");
                        }
                    } else {
                        editText_mensaje.setError("Debe de escribir un mensaje");
                    }
                }
                else
                {
                    Intent intent = new Intent(BandejaActivity.this, OptionsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        cardView_ButtonMensajes = findViewById(R.id.cardView_ButtonMensajes);
        cardView_ButtonMensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                editText_nombre.getEditText().setText("");
                editText_mensaje_recibido.getEditText().setText("");
                spinner_send_to.getEditText().setText("");
                titulo="nulo";
                mensaje="nulo";
               Intent intent = new Intent(BandejaActivity.this, MensajesActivity.class);

               startActivity(intent);
            }
        });



    }

    private void cargarMensaje()
    {
        if(mensaje!=null && titulo!=null)
        {
            Objects.requireNonNull(editText_nombre.getEditText()).setText(titulo);
            Objects.requireNonNull(editText_mensaje_recibido.getEditText()).setText(mensaje);

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