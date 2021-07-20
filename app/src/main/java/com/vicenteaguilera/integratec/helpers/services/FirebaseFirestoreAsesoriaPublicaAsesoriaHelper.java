package com.vicenteaguilera.integratec.helpers.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.vicenteaguilera.integratec.controllers.MainAdviserActivityApp;
import com.vicenteaguilera.integratec.controllers.OptionsActivity;
import com.vicenteaguilera.integratec.helpers.utility.helpers.AlertDialogPersonalized;
import com.vicenteaguilera.integratec.helpers.utility.helpers.DateHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.InternetHelper;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesores;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesorias;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.Asesor;
import com.vicenteaguilera.integratec.models.Asesoria;
import com.vicenteaguilera.integratec.models.RealtimeAsesoria;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class FirebaseFirestoreAsesoriaPublicaAsesoriaHelper
{
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final CollectionReference AsesoriaPublicaCollection = db.collection("asesoria_publica");
    public final CollectionReference AsesoriaCollection = db.collection("asesoria");





    /***********************************************
     *      ASESORIA_PUBLICA COLLECTION
     ***********************************************/
    /**
     * Registrar asesoría publica en firebase
     * @param document
     * @param status
     * @param dialog
     * @param data
     * @param estado
     */
    public void registerDataAsesoriaPublicaToFirestore(String document, final Status status, final ProgressDialog dialog, final Map<String, Object> data, boolean estado)
    {
        // Add a new document with a generated ID
        if(estado) {
            AsesoriaPublicaCollection.document(document).set(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                status.status("asesoría ajustada y publicada para los alumnos");
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                                status.status("Error del registro de los datos. Inténtelo de nuevo");

                            }
                        }
                    });
        }
        else
        {

            AsesoriaPublicaCollection.document(FirebaseFirestoreAsesorHelper.asesor.getUid())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            status.status("asesoría terminada...");
                            addAsesoriaData(String.valueOf(data.get("materia")),String.valueOf(data.get("fecha")),String.valueOf(data.get("h_inicio")),String.valueOf(data.get("h_final")),status);
                            dialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            status.status("Error del eliminación de asesoría. Inténtelo de nuevo");
                        }
                    });
        }
    }

    /**
     * Método que escucha cuando la lista de asesores se actualia y actualiza la lista de asesores disponibles
     * @param listaAsesores es una interfaz que permite escuchar los cambios y enviar la lista a la actividad
     *                      de asesores disponibles {@link com.vicenteaguilera.integratec.controllers.ListAdviserActivity}
     *
     */
    public void listenAsesorias(final ListaAsesores listaAsesores)
    {
        final List<RealtimeAsesoria> realtimeAsesoriaList = new ArrayList<>();
        final RealtimeAsesoria[] realtimeAsesoria = new RealtimeAsesoria[1];

        AsesoriaPublicaCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }
                        //como la lista se vuelve final si y si entrara por segunda vez y no hay el clear va a contener los elementos pasados + los actuales
                        //y ocaciones que haya repetidos
                        realtimeAsesoriaList.clear();
                        final int size = Objects.requireNonNull(snapshots).getDocuments().size();
                        Log.e("Documets: ", size+"");
                        boolean colocar=false;
                        for (final DocumentSnapshot dc : snapshots.getDocuments())
                        {
                                    final Map<String,Object> asesoria_add =  dc.getData();
                                    try {
                                         colocar = DateHelper.expiracionFecha(asesoria_add.get("fecha").toString(), asesoria_add.get("h_final").toString());
                                    } catch (ParseException parseException) {
                                        parseException.printStackTrace();
                                    }
                                    if(!colocar)
                                    {
                                        realtimeAsesoria[0] = new RealtimeAsesoria(dc.getId(),asesoria_add.get("lugar").toString(),
                                                asesoria_add.get("URL").toString(), asesoria_add.get("materia").toString(), asesoria_add.get("h_inicio").toString(),
                                                asesoria_add.get("h_final").toString(), asesoria_add.get("informacion").toString(),asesoria_add.get("fecha").toString(), asesoria_add.get("nombre").toString(), asesoria_add.get("image_asesor").toString());
                                        realtimeAsesoriaList.add(realtimeAsesoria[0]);
                                    }
                        }
                        listaAsesores.getAsesoresRealtime(realtimeAsesoriaList);
                    }
                });
    }

    /***********************************************
     *      ASESORIAS COLLECTION
     ***********************************************/
    /**
     * Agrega la asesoría para guardarla cuando la asesoría publica termina
     * @param materia
     * @param fecha
     * @param h_inicio
     * @param h_final
     * @param status
     */
    public void addAsesoriaData(String materia, String fecha, String h_inicio,String h_final,final Status status)
    {
        Map<String, Object> asesoria = new HashMap<>();
        asesoria.put("asesor",FirebaseFirestoreAsesorHelper.asesor.getUid());
        asesoria.put("nombre", (FirebaseFirestoreAsesorHelper.asesor.getNombre()+" "+FirebaseFirestoreAsesorHelper.asesor.getApellidos()).toUpperCase());
        asesoria.put("materia", materia.toUpperCase());
        asesoria.put("fecha", fecha);
        asesoria.put("h_inicio", h_inicio.toUpperCase());
        asesoria.put("h_final", h_final.toUpperCase());
        AsesoriaCollection.add(asesoria).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isComplete())
                {
                    status.status("¡Asesoría terminada!");
                }
            }
        });

    }

    /**
     * Obtiene todas las asesorías relacionadas con el asesor para crear los pdfs
     * @param listaAsesorias
     */
    public void listenGetAsesoriasData(final ListaAsesorias listaAsesorias)
    {
        final List<Asesoria> asesoriaList = new ArrayList<>();
        final Asesoria[] asesorias = new Asesoria[1];
        AsesoriaCollection
                .whereEqualTo("asesor", FirebaseFirestoreAsesorHelper.asesor.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                final Map<String,Object> asesoria_add =  document.getData();
                                Date date = null;
                                try {
                                     date = new SimpleDateFormat("dd-MM-yyyy").parse(asesoria_add.get("fecha").toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                asesorias[0] = new Asesoria(asesoria_add.get("asesor").toString(), asesoria_add.get("nombre").toString(),
                                        asesoria_add.get("materia").toString(), date,
                                        asesoria_add.get("h_inicio").toString(), asesoria_add.get("h_final").toString());
                                asesoriaList.add(asesorias[0]);
                            }
                            listaAsesorias.getAsesorias(asesoriaList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Elimina la asesorías relacionadas con el asesor cuando el semestre termina
     */
    public void deleteAsesoriasData()
    {
        AsesoriaCollection
                .whereEqualTo("asesor", FirebaseFirestoreAsesorHelper.asesor.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                               document.getReference().delete();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
