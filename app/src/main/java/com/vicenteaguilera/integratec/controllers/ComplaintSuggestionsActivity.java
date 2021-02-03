package com.vicenteaguilera.integratec.controllers;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.helpers.utility.helpers.ButtonHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.PropiertiesHelper;
import com.vicenteaguilera.integratec.helpers.utility.SenderAsyncTask;
import com.vicenteaguilera.integratec.helpers.utility.helpers.StringHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.WifiReceiver;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class ComplaintSuggestionsActivity extends AppCompatActivity implements View.OnClickListener {
    String topic="queja";
    private MaterialButton cardView_ButtonEnviar;
    private RadioButton radioButton_queja,radioButton_sugerencia;
    private RadioGroup radioGroup_topic;
    private TextInputLayout editText_email;
    private TextInputLayout editText_mensaje;
    private TextInputLayout editText_nombre;
    private ButtonHelper buttonHelper = new ButtonHelper();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_suggestions);

        editText_email = findViewById(R.id.editText_email);
        editText_mensaje = findViewById(R.id.editText_mensaje);
        editText_nombre = findViewById(R.id.editText_nombre);

        radioButton_sugerencia =findViewById(R.id.radioButton_sugerencia);
        radioButton_queja = findViewById(R.id.radioButton_queja);

        radioGroup_topic = findViewById(R.id.radioGroup_topic);
        cardView_ButtonEnviar = findViewById(R.id.cardView_ButtonEnviar);
        cardView_ButtonEnviar.setOnClickListener(this);

        radioButtonListener();
    }

    private void radioButtonListener() {
        radioGroup_topic.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radiobutton) {

                RadioButton checkedRadioButton = radioGroup.findViewById(radiobutton);
                boolean isChecked = checkedRadioButton.isChecked();

                if(isChecked)
                {
                    if(checkedRadioButton.getId()==R.id.radioButton_queja)
                    {
                       topic= getResources().getString(R.string.queja).toLowerCase();

                    }else if(checkedRadioButton.getId()==R.id.radioButton_sugerencia)
                    {
                        topic= getResources().getString(R.string.sugerencia).toLowerCase();
                    }
                    else
                    {
                        topic="";
                    }
                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.cardView_ButtonEnviar:

                boolean flagEmail=false;
                boolean flagMensaje=false;
                boolean flagNombre=false;

                //evaluación de nombre
                if(!editText_nombre.getEditText().getText().toString().isEmpty()) {
                    flagNombre=true;
                }
                else{
                    editText_nombre.setError("Debes ingresar tu nombre completo.");
                }

                //evaluación de email
                if(!editText_email.getEditText().getText().toString().isEmpty()) {
                    if(new StringHelper().isEmail(editText_email.getEditText().getText().toString())) {
                        flagEmail=true;
                    }
                    else {
                        editText_email.setError("Correo electrónico inválido.");
                    }
                }
                else {
                    editText_email.setError("Debes ingresar un correo electrónico  para recibir respuesta a tu petición.");
                }

                //evaluación de mensaje
                if(!editText_mensaje.getEditText().getText().toString().isEmpty()) {
                    flagMensaje=true;
                }
                else{
                    editText_mensaje.setError("Debes ingresar una queja o sugerencia.");
                }

                if(!topic.equals("") && flagEmail && flagMensaje && flagNombre)
                {
                    String datos[] = new String[4];
                    datos[0] = editText_nombre.getEditText().getText().toString();
                    datos[1] = editText_email.getEditText().getText().toString();
                    datos[2] = topic;
                    datos[3] = editText_mensaje.getEditText().getText().toString();
                    Snackbar.make(view, getResources().getText(R.string.agradecimientos) +" "+ topic, Snackbar.LENGTH_SHORT).show();
                    sendEmailWithGmail(PropiertiesHelper.EMAIL, PropiertiesHelper.PASSWORD, editText_email.getEditText().getText().toString(), view.getContext(), datos);
                    editText_nombre.getEditText().setText("");
                    editText_mensaje.getEditText().setText("");
                    editText_email.getEditText().setText("");
                    editText_nombre.setError(null);
                    editText_mensaje.setError(null);
                    editText_email.setError(null);
                    radioButton_queja.setChecked(true);
                }
                break;
        }
    }

    private void sendEmailWithGmail(final String from, final String passwordfrom, String to, Context context, String[]datos) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, passwordfrom);
            }
        });

        SenderAsyncTask task = new SenderAsyncTask(session,from,to,context,datos);
        task.execute();
    }
}