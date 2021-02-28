package com.vicenteaguilera.integratec.helpers.services;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vicenteaguilera.integratec.helpers.utility.helpers.AlertDialogPersonalized;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesorados;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesorias;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.Alumno;
import com.vicenteaguilera.integratec.models.Asesorado;
import com.vicenteaguilera.integratec.models.Asesoria;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FirestoreAsesorado {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference AsesoradosCollection = db.collection("asesorados");
    public static Alumno alumno = null;
    private  AlertDialogPersonalized dialogPersonalized = new AlertDialogPersonalized();

    //Registra asesorados y se vinculan al asesor.
    public void addAsesorado(final Status status, final ProgressDialog dialog, final Context context, String numControl, String nombre, String carrera, String materia, String tema, String fecha, String idAsesor)
    {
        final Map<String, Object> asesorado = new HashMap<>();
        asesorado.put("numeroControl", numControl);
        asesorado.put("nombre", nombre);
        asesorado.put("carrera", carrera);
        asesorado.put("materia", materia);
        asesorado.put("tema", tema);
        asesorado.put("fecha",fecha );
        asesorado.put("idAsesor", idAsesor);

        AsesoradosCollection.add(asesorado).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                dialog.dismiss();
                if(task.isSuccessful()){
                    dialogPersonalized.alertDialogInformacion("Datos de asesorado registrados con exito.", context);
                }else {
                    dialogPersonalized.alertDialogInformacion("Error al registrar los datos del asesorado. Inténtelo de nuevo.", context);
                }
            }
        });
    }

    public void updateDataAsesorado(final Status status, final ProgressDialog dialog, final Context context, final String idAsesorado, String numControl, String nombre, String carrera, String materia, String tema, String fecha, String idAsesor) {
        final Map<String, Object> asesorado = new HashMap<>();
        asesorado.put("numeroControl", numControl);
        asesorado.put("nombre", nombre);
        asesorado.put("carrera", carrera);
        asesorado.put("materia", materia);
        asesorado.put("tema", tema);
        asesorado.put("fecha", fecha);
        asesorado.put("idAsesor", idAsesor);

        AsesoradosCollection.document(idAsesorado).update(asesorado)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialogPersonalized.alertDialogInformacion("Se actualizarón los datos del asesorado.", context);
                        //status.status("Se actualizarón los datos del asesorado.");
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        dialogPersonalized.alertDialogInformacion("No se actualizarón los datos del asesorado, verifica tu conexión a Internet.", context);
                        //status.status("No se actualizarón los datos del asesorado, verifica tu conexión a Internet");
                        dialog.dismiss();
                    }
                });
    }

    public void deleteDataAsesorado(final Status status, final ProgressDialog dialog, final Context context, final String idAsesorado) {
        AsesoradosCollection.document(idAsesorado).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialogPersonalized.alertDialogInformacion("Se eliminarón los datos del asesorado.", context);
                        //status.status("Se actualizarón los datos del asesorado.");
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        dialogPersonalized.alertDialogInformacion("No se eliminarón los datos del asesorado, verifica tu conexión a Internet.", context);
                        //status.status("No se actualizarón los datos del asesorado, verifica tu conexión a Internet");
                        dialog.dismiss();
                    }
                });
    }

    //Elimina los registros de asesorados pertenecientes al asesor.
    public void deleteAsesorados(String idAsesor, final ProgressDialog dialog)
    {
        AsesoradosCollection
                .whereEqualTo("idAsesor", idAsesor)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                document.getReference().delete();
                            }
                            dialog.dismiss();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //Lee y obtiene los alumnos asesorados que el asesor asesoró.
    public void readAsesorados(final ListaAsesorados listaAsesorados, String idAsesor)
    {
        final List<Asesorado> asesoradosList = new ArrayList<>();
        final Asesorado[] asesorados = new Asesorado[1];
        AsesoradosCollection
                .whereEqualTo("idAsesor", idAsesor)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                final Map<String,Object> asesorado_add =  document.getData();

                                Date date = null;
                                try {
                                    date = new SimpleDateFormat("dd/MM/yyyy").parse(asesorado_add.get("fecha").toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                asesorados[0] = new Asesorado(document.getId(), asesorado_add.get("nombre").toString(), asesorado_add.get("numeroControl").toString(), asesorado_add.get("carrera").toString(),
                                        asesorado_add.get("tema").toString(), date, asesorado_add.get("materia").toString(), asesorado_add.get("idAsesor").toString());
                                asesoradosList.add(asesorados[0]);
                            }
                            listaAsesorados.getAsesorados(asesoradosList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
