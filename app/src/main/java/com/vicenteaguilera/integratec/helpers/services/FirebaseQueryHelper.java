package com.vicenteaguilera.integratec.helpers.services;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vicenteaguilera.integratec.helpers.utility.PropiertiesHelper;
import com.vicenteaguilera.integratec.helpers.utility.SenderAsycTask;

import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class FirebaseQueryHelper
{
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference AsesoresCollection = db.collection("asesores");

    public void BuscarCredenciales(String email, final Context context)
    {
        final ProgressDialog dialog = ProgressDialog.show(context, "",
                "Verificando en el sistema", true);
        dialog.show();
        AsesoresCollection.whereEqualTo("email",email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                dialog.dismiss();

                if(queryDocumentSnapshots.getDocuments().size()>0)
                {

                    Map<String,Object> mapData= queryDocumentSnapshots.getDocuments().get(0).getData();
                    String [] datos = {mapData.get("nombre")+" "+mapData.get("apellido"),String.valueOf(mapData.get("password"))};

                    sendEmailWithGmail2(PropiertiesHelper.EMAIL,PropiertiesHelper.PASSWORD,String.valueOf(mapData.get("email")),context,datos);

                }
                else {
                    Toast.makeText(context, "No existe registro con esos datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void sendEmailWithGmail2(final String from, final String passwordfrom,String to,Context context,String[]datos) {
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

        SenderAsycTask task = new SenderAsycTask(session, from,to,context,datos);
        task.execute();
    }
}
