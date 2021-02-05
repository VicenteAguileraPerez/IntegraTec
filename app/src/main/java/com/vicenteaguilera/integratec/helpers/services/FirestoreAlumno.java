package com.vicenteaguilera.integratec.helpers.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vicenteaguilera.integratec.controllers.OptionsActivity;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.Alumno;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirestoreAlumno{
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference AlumnoCollection = db.collection("alumno");
    public static Alumno alumno = null;

    public void addDataAlumno(final Status status, final ProgressDialog dialog, final String num, String nombre, String carrera, final Context context){
        final Map<String, Object> alumno = new HashMap<>();
        alumno.put("nombre", nombre);
        alumno.put("carrera",carrera);

        AlumnoCollection.document(num).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    if (task.getResult().exists()){
                        final AlertDialog.Builder  alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setTitle("Aviso");
                        alertDialogBuilder.setMessage("El número de control ya existe en la base de datos");

                        alertDialogBuilder.setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface alertDialog, int i)
                                    {
                                        alertDialog.dismiss();
                                    }
                                }
                        );

                        dialog.dismiss();
                        alertDialogBuilder.show();
                    }else {
                        registerDataAlumnoToFirestore(num, status, dialog, alumno, context);
                    }
                }else {
                    status.status("Error, verifique su conexión a Internet, si los problemas continuan contacte al administrado");
                    Toast.makeText(context, "no es successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void registerDataAlumnoToFirestore(final String document, final Status status, final ProgressDialog progressDialog, final Map<String, Object> data, final Context context){
        AlumnoCollection.document(document).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();

                final AlertDialog.Builder  alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setTitle("Aviso");

                alertDialogBuilder.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface alertDialog, int i)
                            {
                                alertDialog.dismiss();
                            }
                        }
                );

                if(task.isSuccessful()){
                    alertDialogBuilder.setMessage("Alumno registrado con exito...");
                }else {
                    alertDialogBuilder.setMessage("Error del registros de los datos. Inténtelo de nuevo");
                    status.status("Error del registros de los datos. Inténtelo de nuevo");
                }

                alertDialogBuilder.show();
            }
        });
    }

    public void getDataAlumno(String document, final ProgressDialog dialog, final Status status, final Context context){

        AlumnoCollection.document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                dialog.dismiss();
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(Objects.requireNonNull(document).exists()){
                        Map<String,Object> data = document.getData();
                        alumno = new Alumno(document.getId(), data.get("nombre").toString(), data.get("carrera").toString());

                    }else {
                        status.status("No existe esa cuenta");
                        final AlertDialog.Builder  alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setTitle("Aviso");
                        alertDialogBuilder.setMessage("El alumno buscado no existe en la base de datos");

                        alertDialogBuilder.setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface alertDialog, int i)
                                    {
                                        alertDialog.dismiss();
                                    }
                                }
                        );
                        dialog.dismiss();
                        alertDialogBuilder.show();


                    }
                }else {
                    status.status("Error, verifique su conexión a Internet, si los problemas continuan contacte al administrado");
                }

            }
        });
    }

    public void updateDataAlumno(final String num,final String nombre, final String carrera, final ProgressDialog dialog, final Status status, final Context context){
        Map<String,Object> alumno = new HashMap<>();
        alumno.put("nombre", nombre);
        alumno.put("carrera", carrera);

        AlumnoCollection.document(num).update(alumno).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                final AlertDialog.Builder  alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setTitle("Aviso");
                alertDialogBuilder.setMessage("Alumno actualizado con éxito");

                alertDialogBuilder.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface alertDialog, int i)
                            {
                                alertDialog.dismiss();
                            }
                        }
                );

                alertDialogBuilder.show();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                status.status("Datos no actualizados, verifica tu conexión a Internet");
                dialog.dismiss();
            }
        });
    }



}
