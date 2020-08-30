package com.vicenteaguilera.integratec.helpers.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vicenteaguilera.integratec.controllers.MainAdviserActivityApp;
import com.vicenteaguilera.integratec.controllers.OptionsActivity;
import com.vicenteaguilera.integratec.helpers.utility.ListaAsesores;
import com.vicenteaguilera.integratec.helpers.utility.Status;
import com.vicenteaguilera.integratec.models.Asesor;
import com.vicenteaguilera.integratec.models.RealtimeAsesoria;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class FirestoreHelper
{
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference AsesoresCollection = db.collection("asesores");
    private final CollectionReference AsesoriaPublicaCollection = db.collection("asesoria_publica");
    //private static Status status;
    //private ProgressDialog dialog;
    public static Asesor asesor=null;
    //agrega los datos la primera vez
    public void addData(Status status, ProgressDialog dialog, Context context, String uid, String email, String password, String[] param)
   {
       if(param.length==3)
       {
           //asesor
           Map<String, Object> asesor = new HashMap<>();
           asesor.put("nombre", param[0]);
           asesor.put("apellido", param[1]);
           asesor.put("carrera", param[2]);
           asesor.put("email", email);
           asesor.put("password", password);
           asesor.put("uri_image", "");
           asesor.put("activo",false);
           Log.e("lista",  asesor.values().toArray().length+"");
           registerDataUserToFirestore(AsesoresCollection,uid,status,dialog, asesor,context);

       }
       else if(param.length==2)
       {
           //es un profesor
       }
   }
   //registra
   private void registerDataUserToFirestore(CollectionReference collectionReference, final String document, final Status status, final ProgressDialog dialog, final Map<String, Object> data, final Context context)
   {
       // Add a new document with a generated ID
       collectionReference.document(document).set(data)
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task)
                       {
                           if(task.isSuccessful())
                           {
                               status.status("Registrado comuníquese con el administrador para habilitar su cuenta...");
                               Intent intent = new Intent(context, OptionsActivity.class);
                               context.startActivity(intent);
                           }
                           else
                           {
                               status.status("Error del registro de los datos. Inténtelo de nuevo");
                           }
                           dialog.dismiss();
                       }
               });
   }
   //getAsesor
    public void getData(String document, final ProgressDialog dialog, final Status status,final Context context)
    {
        dialog.show();
        AsesoresCollection.document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (Objects.requireNonNull(document).exists())
                    {
                        Map<String,Object>data=document.getData();
                        asesor = new Asesor(document.getId(),String.valueOf(data.get("nombre")),String.valueOf(data.get("apellido")),String.valueOf(data.get("email")),String.valueOf(data.get("password")),String.valueOf(data.get("carrera")),String.valueOf(data.get("uri_image")),(boolean)data.get("activo"));
                        if((boolean)document.get("activo"))
                        {
                            status.status("Bienvenido "+asesor.getNombre()+" "+asesor.getApellidos());
                            Intent intent = new Intent(context, MainAdviserActivityApp.class);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }
                        else
                        {
                            if(!(((Activity)context) instanceof OptionsActivity))
                            {

                                status.status("Usted se ha registrado, comuníquese con el administrador para habilitar su cuenta...");
                                Intent intent = new Intent(context, OptionsActivity.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
                            else{
                                status.status("Usted se ha registrado, comuníquese con el administrador para habilitar su cuenta...");
                            }
                        }
                    }
                    else
                    {
                        status.status("No existe esa cuenta");
                    }
                }
                else
                {
                    status.status("Error, verifique su conexión a Internet, si los problemas continuan contacte al administrado");
                }
                dialog.dismiss();
            }
        });

    }
    public void updateDataAsesor(final String nombre, final String apellido, final String carrera, final ProgressDialog dialog, final Status status)
    {
        Map<String,Object> asesor = new HashMap<>();
        asesor.put("nombre", nombre);
        asesor.put("apellido", apellido);
        asesor.put("carrera", carrera);
        AsesoresCollection.document(FirestoreHelper.asesor.getUid()).update(asesor)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirestoreHelper.asesor.setNombre(nombre);
                FirestoreHelper.asesor.setApellidos(apellido);
                FirestoreHelper.asesor.setCarrera(carrera);
                status.status("Datos actualizados");
                dialog.dismiss();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e)
             {
                status.status("Datos no actualizados, verifica tu conexión a Internet");
                dialog.dismiss();
             }
        });

    }
    public void updateImageAsesor(final String uri_image, final Status status)
    {
        Map<String,Object> asesor = new HashMap<>();
        asesor.put("uri_image", uri_image);

        AsesoresCollection.document(FirestoreHelper.asesor.getUid()).update(asesor)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirestoreHelper.asesor.setuRI_image(uri_image);
                        status.status("Datos actualizados");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        status.status("Imagen no actualizada, verifica tu conexión a Internet");
                    }
                });

    }



    public void registerDataAsesoriaPublicaToFirestore(String document, final Status status, final ProgressDialog dialog, Map<String, Object> data,boolean estado, final Context context)
    {
        // Add a new document with a generated ID
        if(estado) {
            AsesoriaPublicaCollection.document(document).set(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                status.status("asesoría pública...");
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
            AsesoriaPublicaCollection.document(asesor.getUid())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            status.status("asesoría terminada...");
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
    public void getAsesoriaData( final Status status)
    {
        //futura implementación quizá
    }
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

                        for (final DocumentChange dc : Objects.requireNonNull(snapshots).getDocumentChanges())
                        {

                            switch (dc.getType()) {
                                case ADDED:
                                case MODIFIED:


                                    final Map<String,Object> asesoria_add =  dc.getDocument().getData();
                                    realtimeAsesoria[0] = new RealtimeAsesoria(dc.getDocument().getId(),asesoria_add.get("lugar").toString(),
                                            asesoria_add.get("URL").toString(), asesoria_add.get("materia").toString(), asesoria_add.get("h_inicio").toString(),
                                            asesoria_add.get("h_final").toString(), asesoria_add.get("informacion").toString(),asesoria_add.get("fecha").toString(), asesoria_add.get("nombre").toString(), asesoria_add.get("image_asesor").toString());
                                    realtimeAsesoriaList.add(realtimeAsesoria[0]);
                                    break;
                                case REMOVED:
                                    break;
                            }
                        }

                        listaAsesores.getAsesoresRealtime(realtimeAsesoriaList);

                    }
                });
    }

}
