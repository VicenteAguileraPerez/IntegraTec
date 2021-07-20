package com.vicenteaguilera.integratec.helpers.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.vicenteaguilera.integratec.controllers.BandejaActivity;
import com.vicenteaguilera.integratec.controllers.OptionsActivity;
import com.vicenteaguilera.integratec.helpers.utility.helpers.DateHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.StaticHelper;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesorias;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.Mensaje;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FirebaseFirestoreMensajesHelper
{
    public static final CollectionReference MensajesCollection = FirebaseFirestoreAsesoriaPublicaAsesoriaHelper.db.collection("mensajes");
    public void mensajeAsesor(String to, String mensaje, final Status status, final ProgressDialog dialog, Context context)
    {
        String from = FirebaseFirestoreAsesorHelper.asesor.getNombre()+" "+FirebaseFirestoreAsesorHelper.asesor.getApellidos();
        Map<String, Object> mensajeMap = new HashMap<>();
        mensajeMap.put("fecha", DateHelper.obtenerFecha());
        mensajeMap.put("from", from);
        mensajeMap.put("to", to);
        mensajeMap.put("mensaje", mensaje);

        MensajesCollection.document(UUID.randomUUID().toString()).set(mensajeMap)
            .addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    dialog.dismiss();
                    if(task.isSuccessful())
                    {
                        new SendNotification().sendAllAsesores(to+","+from, "Te envió un mensaje",mensaje,context);
                        status.status("Mensaje enviado");
                    }
                    else
                    {
                        status.status("Error de envio. Inténtelo de nuevo (verifique su conexión)");
                    }

                }
            });

    }
    public void mensajeTodos(String toName,String fecha, String from, String mensaje, final Status status, final ProgressDialog dialog)
    {

        /*Map<String, Object> mensajeMap = new HashMap<>();
        mensajeMap.put("fecha", fecha);
        mensajeMap.put("from", from);
        mensajeMap.put("to",toName );
        mensajeMap.put("mensaje", mensaje);

        MensajesCollection.document(UUID.randomUUID().toString()).set(mensajeMap)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                        dialog.dismiss();
                        if(task.isSuccessful())
                        {
                            status.status("Mensaje enviado");
                        }
                        else
                        {
                            status.status("Error de envio. Inténtelo de nuevo (verifique su conexión)");
                        }

                    }
                });*/

    }

    /**
     * retorna los mensajes del asesor
     * @param context contexto para mostrar el progress
     * @param listaAsesorias interfaz que retorna los mensajes
     */
    public void BuscarTusMensajes(final Context context, final ListaAsesorias listaAsesorias)
    {
        final ProgressDialog dialog = ProgressDialog.show(context, "",
                "Buscando mensajes", true);
        dialog.show();

        MensajesCollection.whereEqualTo("to",FirebaseFirestoreAsesorHelper.asesor.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                dialog.dismiss();
                ArrayList<Mensaje> mensajes = new ArrayList<>();
                if(queryDocumentSnapshots.getDocuments().size()>0)
                {
                    for (int i = 0; i <queryDocumentSnapshots.getDocuments().size() ; i++)
                    {
                        Map<String,Object> mensajeRecibido = queryDocumentSnapshots.getDocuments().get(i).getData();
                        Mensaje mensaje = new Mensaje(queryDocumentSnapshots.getDocuments().get(i).getId(),mensajeRecibido.get("fecha").toString(),mensajeRecibido.get("from").toString(),mensajeRecibido.get("mensaje").toString());
                        mensajes.add(mensaje);
                    }
                   listaAsesorias.getMensajes(mensajes);
                }
                else {
                    Toast.makeText(context, "No hay mensajes por ahora", Toast.LENGTH_SHORT).show();
                    listaAsesorias.getMensajes(mensajes);
                }
            }
        });
    }
    public void eliminarMensaje(String document,final Context context,Status status)
    {
        final ProgressDialog dialog = ProgressDialog.show(context, "",
                "Eliminando", true);
        dialog.show();
        MensajesCollection.document(document)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        status.status("Eliminación exitosa");
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                      status.status("No se pudo eliminar");
                        dialog.dismiss();
                    }
                });
    }
}
